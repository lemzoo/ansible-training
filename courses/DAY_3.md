DAY 2: 20-03-2019
=================

Advanced Playbook
=================

Structure:
=========

    Import and include
    import_tasks and import_playbok is static
    include_tasks and include_playbook is dynamic
    
    STATIC: Parse time
        - Can't use inventory file vars
        
    DYNAMIC: EXECUTION TIME
        - great for loops
        
    Example: 
    ---
    tasks:
        - import_tasks: tasks/install_apache.yaml
        - import_tasks: tasks/remove_defaults.yaml
        - include_tasks: tasks/setup_{{ansible_os_family | lower}}.yaml
        
    ...
    
    Can also Pass Variables
    ---
    tasks:
        - import_tasks: includes/remove_defaults.yaml
          vars:
            - include_modules: false
    
    MAIN YAML
    ---
    - import_playbook: redis.yaml
    - import_playbook: auth-server.yaml
    - import_playbook: tvseries-db.yaml
    - import_playbook: tvseries-api.yaml

    DIRECTORY STRUCTURE EXAMPLE1
    production                  # inventory
    staging                     # inventory
    group_vars                  # variables for servers groups
       nginx.yaml
       postgres-servers.yaml
    host_vars
        proxy.example.com       # variables for specific hosts
    main.yaml                   # main playbook (with import_playbook)
    databases.yaml              # playbook for databases
    webservers.yaml             # playbook for web servers
    
    ANOTHER STRUCTURE
    production                  # inventory
        group_vars
            nginx.yaml
            postgres-servers.yaml
        host_vars
            proxy.example.com       # variables for specific hosts
            
    staging                     # inventory
        group_vars
            nginx.yaml
            postgres-servers.yaml
        host_vars
            proxy.example.com       # variables for specific hosts
            
    host_vars
        proxy.example.com       # variables for specific hosts
    main.yaml                   # main playbook (with import_playbook)
    databases.yaml              # playbook for databases
    webservers.yaml             # playbook for web servers
    

JINJA TEMPLATE:
==============

    - name: Jinja
      hosts: localhost
      
    debug:
    msg: "{{hostvars['n1'].ansible}}"
    
    ansible localhost -e "v=hello" -m debug -a "msg={{v | upper}}"
    

LOOPS:
=====
    
Traditional Loops:
================= 
   
    - name: Ensure proxy set in .bashrc
        lineinfile:
          path: "{{ ansible_env.HOME }}/.bashrc"
          line: export {{ item }} = {{ proxy_addresss }}
          insertbefore: EOF
          create: yes
        loop:
          - http_proxy
          - HTTP_PROXY
          
    CONDITIONAL:
    
    tasks:
      - name: Ensure proxy has been set for the docker daemon

Abort The whole play:
====================

    - hosts: rest_servers
      any_error_fatal: true
      tasks:
        -name: ...
         ...
         
         or use 
         

FORCE HANDLERS
==============
    
    Ignore Failures
    - name: Ensure database is Initialised
      command: sqltool {{sql_file}}
      ignrore_errors: yes
    
## Dealing with zero return code errors using failed_when
    
    - name: JunOS Commands
      junos_comand:
        commands:
          - show version
          
        register: results
        failed_when:
          results.version <= 1.9
    
## Rescue/always

    tasks:
      - name: Take VM out of the load balancer
      - name: Create a VM snapshot before the app upgrade
      - block: 
          - name: Upgrade the application
          - Run smoke tests
        rescue:
          - name: Revert a VM to the snapshot after a failed upgrade
        always:
          - name: Re-add webserver to the loadbalancer
          - ame: Remove a VM snapshot
          
# Open source server: emby (have a look), plex

# Check also: pygments, ycube
      
# Parallelisme:

    Linear: By default, the parallelisme is set to 3.
    Free: Use available worker to run the next task
    Example: 
    ---
    hosts: all
    strategy: free
    
    Batch: 
    ---
    hosts: all
    serial: 3
    

Task delegation:
================

    Delegate run task on host inventory variables are kept
    ---
    hosts: webservers
    serial: 1
    tasks:
        - name: disable nagios alerts for this host webservers service
          nagios: 'action=disable_alerts host={{ inventory_hostname}}'
          deletegate_to: nagios.example.com
          
        - name: disable the server in haproxy
          haproxy: 'state=disabled hosts={{ inventory_hostname }}'
          delegate_to: lb.example.com
          

TAGS:
====

    Only run specific part of play
    
    ---
    tasks: 
      - package:
          - nginx
          - mysql
        tags:
          - installation
      - pip:
          name: psycopg
        tags:
          - installation
          
     Tags type: always, never (useful for debug mode), untagged (all untagged tasks)
          
    Including tagged tasks
    $ ansible-playbook -tags "configuration" provision.yml
    
 # Hae a lookup : Avoid creating .retry file after run playbook
 
 Roles:
 =====
 
    Reusable playbook with defined structure by ansible
    Structure:
    myrole:                 role name
     - defaults/            
         - main.yml         defaults values for role variables/parameters
     - vars/
          - main.yml        additional variables
     - files                files to deploy/copy
     - templates            templates to deploy/copy
     - tasks
          - main.yml        task list (role's entry point)
     - handlers
           - main.yml       handlers to be triggered (by role and playbook)
     - tests
           - inventory.yml
           - main.yml
 
 Galaxy:
 ======
 
    Plateform to share roles
    https://galaxy.ansible.com/
    
Ansible Tower with AWX:
======================
    
    Has a REST API
    SCM: Source Control Manager access

Use module: firewalld

          