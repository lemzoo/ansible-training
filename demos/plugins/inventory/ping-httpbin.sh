#!/usr/bin/env bash
ansible label_run_httpbin -i kube.yaml -m ping --extra-vars ansible_python_interpreter=/usr/bin/python3
