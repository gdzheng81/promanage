import requests

# LangFlow API 地址
api_url = "http://127.0.0.1:7860/api/v1/run/1c60e6b3-9a84-4f42-92fa-50c8e347d2f2?stream=false"

# 请求头
headers = {
    'Content-Type': 'application/json'
}

# 构建请求体
request_body = {
    "input_value": "message",
    "output_type": "chat",
    "input_type": "text",
    "tweaks": {
        "GitLoaderComponent-xaXda": {
            "repo_source": "Remote",
            "clone_url": "https://github.com/gdzheng81/promanage",
            "branch": "main",
            "file_filter": "*",  # 不过滤文件，加载所有文件
            "content_filter": ""  # 不设置内容过滤
        },
        "ChatOutput-uG0Cn": {},
        "GitChangedComponent-tRlls": {},
        "GitExtractorComponent-Txuor": {}
    }
}

try:
    # 发送 POST 请求
    print("正在发送请求到 API...")
    response = requests.post(api_url, headers=headers, json=request_body)

    # 检查响应状态码
    if response.status_code == 200:
        result = response.json()
        print("请求成功，完整响应结果：")
        print(result)  # 打印完整的响应数据，用于调试

        outputs = result.get('outputs', [])
        if outputs:
            for output in outputs:
                output_data = output.get('outputs', [])
                if output_data:
                    for item in output_data:
                        print("解析当前数据项:")
                        print("数据项内容:", item)
                        print("-" * 50)
                else:
                    print("当前 output 没有有效的数据输出。")
        else:
            print("响应中没有检测到有效的输出信息。")
    else:
        print(f"请求失败，状态码：{response.status_code}，错误信息：{response.text}")
except requests.RequestException as e:
    print(f"请求发生错误：{e}")
    raise  # 重新抛出异常，以便上层调用者可以处理（如果有）