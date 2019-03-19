#!/usr/bin/env python

import json
import sys

import vboxapi

def list_2_dict(a):
    return {k: v for e in a for k, v in e.items()}


