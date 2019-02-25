FROM maven:3.6-jdk-8-alpine as build
WORKDIR /app
COPY /pepper-box /app
RUN mvn clean install -Djmeter.version=3.0 -Dkafka.version=1.0.1 -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine
WORKDIR /app

ENV EH_NS_CONNECTION_STR ""
ENV EH_NAME ""

ENV MACHINE_ID "M-100"
ENV MIN_NUM_OF_SENSORS 5
ENV MAX_NUM_OF_SENSORS 5
ENV MIN_LENGTH_OF_VALUES 50
ENV MAX_LENGTH_OF_VALUES 50

ENV THROUGHPUT_PER_PRODUCER 200
ENV TEST_DURATION 300
ENV NUM_PRODUCERS 10

COPY --from=build /app/target/pepper-box-1.0.jar .
COPY ./conf .
COPY ./start.sh .

ENTRYPOINT ["sh", "-c"]
CMD ["./start.sh"]
