DAY 1: 18-03-2019
=================

Ansible is a DSL: Domain Specific Language.
Example: SQL is also a DSL

Ansible is IDEMPOTENT

    Example: multiplication by 1: Y x 1 = Y


CONTROL MACHINE (Master)
========================

    Ansible Installed
    Playbooks are available
    Created/Managed

REQUIREMENTS on Master:

    - Linux, Unix, MacOS
    - OS package manager (yum, apt, ...)
    - Pip

Source

    Fedora <---- RHel <---- CentOS
    AWX <----- Tower

Inventory
=========

Defines target systems static or dynamic

Example:

    mail.example.com
    
    [webservers]
    web-1.example.com
    web-2.example.com
    
    [dbservers]
    psql-1.example.com
    oracle-1.example.com
    mysql-1.example.com
    
    
Playbook

    --- (Permet de separer les documents YAML)
    --- (Another document)
    - hosts: ...
      ...
    - tasks:
    - name: ensure https is at he latest version
      package:
        name: httpd
        state: present
    - copy:
      src: resources/index.html
      dest: /var/www/html
    - name: ensure apache is running
      service:
        name: httpd
        state: started
   
        
all

    fait reference au group. Il est implicite

    $ ansible all -m copy -a "src=resources/index.html dest=/var/www/html"
    Default module: Command: $ ansible all ""

Groups: (All by default)

    mail.example.com
    web-1.example.com
    web-2.example.com
    psql-1.example.com
    oracle-1.example.com
    mysql-1.example.com
    
Groups with described name

    mail.example.com
    
    [webservers]
    web-1.example.com
    web-2.example.com
    
    [dbservers]
    psql-1.example.com
    oracle-1.example.com
    mysql-1.example.com
    
Groups of groups

    mail.example.com
    
    [webservers]
    web-1.example.com
    web-2.example.com
    
    [dbservers]
    psql-1.example.com
    oracle-1.example.com
    mysql-1.example.com
    
    [centos:children]
    webservers
    dbservers
    
Passing port inventory

    mail.example.com:22
    
    [webservers]
    web-1.example.com:222
    web-2.example.com:1024
 
Passing an inventory
 
    using -i << file >> -i myhosts
    using command -i "mail.host.domain, "
    using default hosts
    
Using  Patterns to hosts
    
    ansible 10.20.* -i inv-range ...
    
Ansible configuration: Three Levels

    3. ansible.cfg
    2. environment variables
    1. command line options (some)
    
    
Ansible config Location

    1. ANSIBLE_CONFIG
    2. ./ansible.cfg*
    3. ~/ansible.cfg
    4. /etc/ansible/ansible.cfg
    

Install cowsay: plugin 

Variables and Facts: Variable is inside play and variable related to the host is the fact

Scopes:

    Global: from configuration & environment variables
    Play var declarations inside playbook
    Host facts, host/group variables, set_fact
    
Used In Jinja2 Templates

    {{ proxy_address }}
    "restarted {{ service_name}}"
    
Play Scope:

    ---
    - hosts: all
      user: vagrant
      vars:
        host: proxyà&:example.com
        port_no: 8443
        proxy_address: http://{{host}}:{{port_no}}
        
      tasks:
      - name: Ensure proxy set in .npmrc
        blockinfile:
          path: /home/vagrant/.npmrc
          block: |
            proxy: 

Types: 

    String
    number
    boolean
    
    dictionnary (json/yaml)/object (js)
    list (json/yaml)/array (js)

Facts are defined inside hosts and variables inside playbook    

Magic Variables

    - hostvars[group] - access vars of other hosts
    - group_names - array of host' groups
    - groups - all groups/hosts
    - ansible_play_hosts - list of active hosts
    
command line example

    ansible command
    
Ansible Playbook

    name
    hosts
    users
    variables
    tasks
    handlers
    
Example:

    ---
    - name: Configure Apache httpd
      hosts: tv-series-web-servers
      become: true
      tasks:
      - name: ensure https is at the latest version
        package:
          name: httpd
          state: present
      ...
      - name: ensure apache is running
        service: httpd
        state: started

Gathering facts is `true/yes` by default

Tasks:

A play contains one or more tasks
Task list
    
    - in order
    - one-at-a-time for all machines
    - then proceed to next task

In case of failures, the host which a failed occurred will be skipped for the next tasks

External variable files:

    hosts:
    vars_files:
        - vars/myvars.yml
        
PROMPTING

    - hosts: localhost
      vars:
        proxy: http://{{username}}:{{password}}@{{host}}:{{port_no}}
      vars_prompt:
      - name: username
        prompt: "proxy username"
        priavet: no
      - name: password
        prompt: "proxy password"
        private: yes
   
REGISTER VARIABLES

    tasks:
    - command: /usr/sbin/ip route list 10.0.2.0/24
      register: routeValue
    - debug:
        var: routeValue

SETTING FACTS USING set_fact

    tasks:
      - command: whoami
        register: woami_result
      - set_fact:
          user: "{{woami_result.stdout}}"
          
set_fact USING JINJA2 FUNCTIONS

    tasks:
    - command: /usr/sbin/ip route list 10.0.2.0/24
      register: route_result
    - set_fact:
        # By default split use espace to split
        route_ip: "{{route_result.stdout.split()[8]}}"
        

Check Mode

    ansible-playbook --check myplaybook.yml
    
DIFF (TOGETHER WITH CHECK-MODE)

    ansible-playbook --check --diff myplaybook.yml
    
STEPPING one-by-step-at-a-time

    ansible-playbook myplaybook.yml --step
    
Make error messages easier to read

    ANSIBLE_STDOUT_CALLBACK=yaml ansible-playbook myplaybook.yml