#!/usr/bin/env bash
while true; do
    for i in {2..6}; do
      ssh vagrant@10.20.1.$i  ps aux | grep -E  "[0-9]{1,2}s$"

    done
    echo "----------------------------"
    sleep 1
 done
