#!/usr/bin/env bash

bundle=ansible-labs
tar  --exclude='target' --exclude="build" --exclude=".*" \
     --exclude='*.iml' \
     --exclude='node_modules' \
     --exclude='cwdecks.postbuild.sh' \
     -czf $DEST/${bundle}.tar.gz  *
