#!/usr/bin/env bash


ARGS=$@

while [[ 1 ]]
do
    /usr/share/d592-server/server ${ARGS}
    read -p "Start a new game session? [y|n] "
    case ${REPLY} in
        y|Y|yes|YES|Yes)    :;;
        *)                  break;;
    esac
    read -p "Leave server run options unchanged? [y|n] "
    case ${REPLY} in
        y|Y|yes|YES|Yes)    continue;;
        *)                  :;;
    esac
    read -p "New server run options (command line arguments): "
    ARGS=${REPLY}
done
