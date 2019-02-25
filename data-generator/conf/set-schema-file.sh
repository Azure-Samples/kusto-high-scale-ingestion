#!/bin/sh

schema_template_file="data-schema-template.json"
schema_file="data-schema.json"

sed "s/MACHINE_ID/$MACHINE_ID/g" $schema_template_file > $schema_file
sed -i "s/MIN_NUM_OF_SENSORS/$MIN_NUM_OF_SENSORS/g" $schema_file
sed -i "s/MAX_NUM_OF_SENSORS/$MAX_NUM_OF_SENSORS/g" $schema_file
sed -i "s/MIN_LENGTH_OF_VALUES/$MIN_LENGTH_OF_VALUES/g" $schema_file
sed -i "s/MAX_LENGTH_OF_VALUES/$MAX_LENGTH_OF_VALUES/g" $schema_file
