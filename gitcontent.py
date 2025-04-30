import git
import shutil
import os
from pathlib import Path
from datetime import datetime

def count_files_in_repo(repo_url, file_extension='.py'):
    temp_dir = Path("temp_repo")
    try:
        # 克隆仓库到临时目录
        git.Repo.clone_from(repo_url, temp_dir)
        repo = git.Repo(temp_dir)
        
        # 获取所有跟踪的文件
        tracked_files = list(repo.git.ls_files().splitlines())
        file_count = len(tracked_files)

        # 统计指定时间段内修改的文件数量
        start_time = datetime(2025, 4, 16, 11, 25)
        end_time = datetime(2025, 4, 28, 10, 20)
        since_date = start_time.strftime("%Y-%m-%d %H:%M:%S")
        until_date = end_time.strftime("%Y-%m-%d %H:%M:%S")

        # 修复：直接使用 git log 命令获取修改的文件列表
        log_output = repo.git.log(
            "--since", since_date,
            "--until", until_date,
            "--name-only",
            "--pretty=format:"
        )
        modified_files = {line for line in log_output.splitlines() if line.strip()}
        modified_file_count = len(modified_files)

        # 修复：正确传递时间参数给 iter_commits
        new_lines_count = 0
        for commit in repo.iter_commits(rev=None, since=since_date, until=until_date):
            for file_path, stats in commit.stats.files.items():
                if file_path.endswith(file_extension):
                    new_lines_count += stats['insertions']

        return file_count, modified_file_count, new_lines_count
    except Exception as e:
        print(f"发生错误: {e}")
        return None, None, None
    finally:
        if temp_dir.exists():
            shutil.rmtree(temp_dir)

if __name__ == "__main__":
    repo_url = "https://github.com/guidezheng1998/code-gen"
    file_extension = '.py'
    file_count, modified_file_count, new_lines_count = count_files_in_repo(repo_url, file_extension)
    if all(v is not None for v in [file_count, modified_file_count, new_lines_count]):
        print(f"仓库中的文件总数为: {file_count}")
        print(f"在指定时间段内修改的文件数量为: {modified_file_count}")
        print(f"在指定时间段内 {file_extension} 文件的新增代码行数为: {new_lines_count}")