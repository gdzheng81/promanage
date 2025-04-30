import requests
import json
from typing import Dict, Any

def call_gitloader_component(
    repo_source: str = "Remote",
    clone_url: str = None,
    repo_path: str = None,
    branch: str = "main",
    file_filter: str = None,
    content_filter: str = None,
    api_key: str = None,
    flow_id: str = "79cba9f2-2c40-42a4-8079-985976d2b7f4"
) -> Dict[str, Any]:
    """
    调用 LangFlow 的 GitLoader 组件
    
    参数:
        repo_source: 仓库来源 "Local" 或 "Remote"
        clone_url: 远程仓库URL (repo_source="Remote"时必填)
        repo_path: 本地仓库路径 (repo_source="Local"时必填)
        branch: 分支名称 (默认 "main")
        file_filter: 文件过滤模式 (如 "*.py,!test_*")
        content_filter: 内容正则过滤模式
        api_key: 可选的API认证密钥
        flow_id: 工作流ID (默认示例ID)
    
    返回:
        API响应数据 (字典格式)
    """
    # API端点
    url = f"http://127.0.0.1:7860/api/v1/run/{flow_id}?stream=false"
    
    # 请求头
    headers = {
        'Content-Type': 'application/json',
    }
    if api_key:
        headers['Authorization'] = f'Bearer {api_key}'
    
    # 构建组件配置
    component_config = {
        "repo_source": repo_source,
        "branch": branch
    }
    
    # 根据来源设置路径
    if repo_source == "Local":
        if not repo_path:
            raise ValueError("repo_path is required for local repository")
        component_config["repo_path"] = repo_path
    elif repo_source == "Remote":
        if not clone_url:
            raise ValueError("clone_url is required for remote repository")
        component_config["clone_url"] = clone_url
    
    # 可选过滤器
    if file_filter:
        component_config["file_filter"] = file_filter
    if content_filter:
        component_config["content_filter"] = content_filter
    
    # 请求体
    payload = {
        "input_value": "git_loader_request",  # 实际输入内容
        "output_type": "text",  # 期望输出类型
        "input_type": "text",   # 输入类型
        "tweaks": {
            "GitLoaderComponent-VGTir": component_config  # 组件ID和配置
        }
    }
    
    try:
        response = requests.post(
            url,
            headers=headers,
            data=json.dumps(payload),
            timeout=30  # 30秒超时
        )
        
        response.raise_for_status()  # 检查HTTP错误
        
        return response.json()  # 返回解析后的JSON响应
        
    except requests.exceptions.RequestException as e:
        print(f"API请求失败: {e}")
        if hasattr(e, 'response') and e.response:
            print(f"错误详情: {e.response.text}")
        return {"error": str(e)}

# 使用示例
if __name__ == "__main__":
    # 示例1: 加载远程仓库
    result = call_gitloader_component(
        repo_source="Remote",
        clone_url="https://github.com/example/repo.git",
        branch="dev",
        file_filter="*.py,*.md",
        content_filter="import requests"
    )
    print("远程仓库加载结果:", json.dumps(result, indent=2))
    
    # 示例2: 加载本地仓库
    # result = call_gitloader_component(
    #     repo_source="Local",
    #     repo_path="/path/to/local/repo",
    #     file_filter="*.txt"
    # )
    # print("本地仓库加载结果:", result)