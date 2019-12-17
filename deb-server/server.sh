#!/usr/bin/env bash


ARGS=$@

while [[ 1 ]]
do
    ./server ${ARGS}
    read -p "Start a new game session? [y|n] "
    case ${REPLY} in
        y|Y|yes|YES|Yes)    :;;
        *)                  break;;
    esac
    read -p "Would you like to change the server configuration? [y|n] "
    case ${REPLY} in
        y|Y|yes|YES|Yes)    :;;
        *)                  continue;;
    esac
    read -p "New server configuration (arguments): "
    ARGS=${REPLY}
done
