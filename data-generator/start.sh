#!/bin/sh

printf "\nRunning set-schema-file.sh :\n"
./set-schema-file.sh

printf "\nRunning set-props-file.sh :\n"
./set-props-file.sh

printf "\nRunning ls -la :\n"
ls -la

printf "\nRunning cat data-schema.json :\n"
cat data-schema.json

printf "\nRunning cat producer.properties :\n"
cat producer.properties

printf "\nRunning Pepper-Box :\n"
java -cp pepper-box-1.0.jar com.gslab.pepper.PepperBoxLoadGenerator --schema-file data-schema.json --producer-config-file producer.properties --throughput-per-producer ${THROUGHPUT_PER_PRODUCER} --test-duration ${TEST_DURATION} --num-producers ${NUM_PRODUCERS}
