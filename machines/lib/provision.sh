#!/usr/bin/env bash


sudo sed -i "s/PasswordAuthentication no/PasswordAuthentication yes/g" /etc/ssh/sshd_config
sudo systemctl restart sshd

sudo yum install -y epel-release
sudo yum install -y python-pip net-tools
sudo sudo pip install psycopg2
