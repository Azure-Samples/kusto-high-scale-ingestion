#!/bin/sh

props_template_file="producer-template.properties"
props_file="producer.properties"

EH_ENDPOINT=$(echo $EH_NS_CONNECTION_STR | cut -d'/' -f 3 | awk '{print $1":9093"}')

sed "s/EH_ENDPOINT/$EH_ENDPOINT/g" $props_template_file > $props_file
sed -i "s,EH_NS_CONNECTION_STR,$EH_NS_CONNECTION_STR,g" $props_file
sed -i "s/EH_NAME/$EH_NAME/g" $props_file
