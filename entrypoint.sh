#!/bin/bash
echo "ls"
ls
echo "pwd"
pwd

npx --package danger danger-kotlin ci --dangerfile /Dangerfile.df.kts
