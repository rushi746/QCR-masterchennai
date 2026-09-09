import os
import shutil

project_dir = r"E:\Rushikesh\MobileApp\NewProject\Q_CSR_chennai\Q-CSR-master\app"
old_package = "com.qikkle.q_csr"
new_package = "com.example.newapp"
old_name = "CSR"
new_name = "NewApp"

old_path = old_package.replace('.', os.sep)
new_path = new_package.replace('.', os.sep)

def replace_text_in_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        if old_package in content or old_name in content:
            content = content.replace(old_package, new_package)
            # Be careful with replacing 'CSR', we only replace 'CSR-dev', 'CSR-dict', 'CSR' where appropriate, but let's just replace old_package first.
            # To be safe, we will specifically replace 'com.qikkle.q_csr' globally.
            # We will handle app name in build.gradle separately if needed, but let's try replacing "CSR" in build.gradle and strings.xml specifically.
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
    except Exception as e:
        pass
    return False

# 1. Replace text in files
for root, dirs, files in os.walk(project_dir):
    for file in files:
        if file.endswith(('.kt', '.java', '.xml', '.gradle', '.pro')):
            filepath = os.path.join(root, file)
            replace_text_in_file(filepath)

# 2. Rename directories
for src_type in ['main', 'androidTest', 'test']:
    base_dir = os.path.join(project_dir, 'src', src_type, 'java')
    old_dir = os.path.join(base_dir, 'com', 'qikkle', 'q_csr')
    new_dir = os.path.join(base_dir, 'com', 'example', 'newapp')
    
    if os.path.exists(old_dir):
        os.makedirs(new_dir, exist_ok=True)
        # Move all contents from old_dir to new_dir
        for item in os.listdir(old_dir):
            shutil.move(os.path.join(old_dir, item), os.path.join(new_dir, item))
        # Remove old directories if empty
        try:
            os.rmdir(old_dir)
            os.rmdir(os.path.join(base_dir, 'com', 'qikkle'))
        except:
            pass

print("Package renamed successfully.")
