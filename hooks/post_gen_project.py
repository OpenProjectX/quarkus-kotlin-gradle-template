#!/usr/bin/env python3
"""Post-generation hook: moves each Kotlin source into its proper package directory.

Cookiecutter can template file *contents* and *names* but not directory paths that
contain dots (a Maven group id like `org.example.foo`). So the sources are emitted
as flat files and this hook relocates them into `<group_id as path>/<sub-package>/`.
"""

import os
import shutil
import stat

group_id = "{{ cookiecutter.group_id }}"
library_name = "{{ cookiecutter.library_name }}"
main_class_name = "{{ cookiecutter.main_class_name }}"

# Maven group id -> directory path (org.example.foo -> org/example/foo)
package_path = group_id.replace(".", "/")

# (gradle module dir, sub-package, flat source file name placed by Cookiecutter)
moves = [
    ("app", "app", f"{main_class_name}Resource.kt"),
    ("runtime", "runtime", f"{main_class_name}Service.kt"),
    ("deployment", "deployment", f"{main_class_name}Processor.kt"),
]

for module, sub_package, filename in moves:
    src_file = os.path.join(module, "src", "main", "kotlin", filename)
    target_dir = os.path.join(module, "src", "main", "kotlin", package_path, sub_package)
    os.makedirs(target_dir, exist_ok=True)
    if os.path.exists(src_file):
        shutil.move(src_file, os.path.join(target_dir, filename))

# Make gradlew executable
gradlew_path = "gradlew"
if os.path.exists(gradlew_path):
    st = os.stat(gradlew_path)
    os.chmod(gradlew_path, st.st_mode | stat.S_IEXEC | stat.S_IXGRP | stat.S_IXOTH)

print(f"\nProject '{{ cookiecutter.project_name }}' generated successfully!")
print(f"  Extension runtime module (the 'starter')      : {group_id}.runtime")
print(f"  Extension deployment module (build-time steps): {group_id}.deployment")
print(f"  Example app                                   : {group_id}.app")
print(f"\nGet started:")
print(f"  cd {{ cookiecutter.project_slug }}")
print(f"  ./gradlew build            # build extension + example app")
print(f"  ./gradlew :app:quarkusDev  # run the example app that consumes the extension")
