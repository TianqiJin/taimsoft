#!/bin/bash

me=$(readlink -e $0)
bin=$(dirname $me)
root=$(readlink -e $bin/..)
cd $root

java -Djdbc.url=$1 -Djdbc.username=$2 -Djdbc.password=$3 -Dserver.port=$4 -jar taim-backend.jar
