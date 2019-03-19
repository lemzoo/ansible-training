#!/usr/bin/env python

import json
import sys

import vboxapi


def list_2_dict(a):
    return {k: v for e in a for k, v in e.items()}


NET_V_IP = "/VirtualBox/GuestInfo/Net/1/V4/IP"

vbox_mgr = vboxapi.VirtualBoxManager(None, None)
vbox = vbox_mgr.getVirtualBox()

machines = list(
    filter(lambda m: m.state == vbox_mgr.constants.MachineState_Running, vbox_mgr.getArray(vbox, 'machines')))
data = list(map(lambda m: (m.name, m.getGuestPropertyValue(NET_V_IP)), machines))

if len(sys.argv) == 2 and sys.argv[1] == '--list':
    print json.dumps(
        {
            "running":
                {
                    "hosts": map(lambda m: m[0], data)
                },
            "_meta":
                {
                    "hostvars": list_2_dict(map(lambda m: {m[0]: {"ansible_host": m[1]}}, data))
                }
        })
elif len(sys.argv) == 3 and sys.argv[1] == '--host':
    print(json.dumps({}))
else:
    print("was expecting --list or --host <name>")


sys.exit(0)
