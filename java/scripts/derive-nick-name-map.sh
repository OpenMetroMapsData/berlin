#!/bin/bash

DIR=$(dirname $0)
CMD="$DIR/maps-berlin.sh"
CLASS="org.openmetromaps.data.berlin.DeriveNickNameMap"

exec "$CMD" "$CLASS" "$@"
