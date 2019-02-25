# Data Generation

In this section we will generate the data that is produced by devices in our factory.
To simulate different devices, rates and data-structure we will use a tool called [Pepper-Box]((https://github.com/GSLabDev/pepper-box)).

## Pepper-Box

Pepper-Box is kafka load generator plugin for jmeter. It allows sending messages to kafka in text format (JSON, XML, CSV or any other custom format) as well as java serialized objects.
Pepper-Box is able to send messages in multi-threaded processes, in a very high rate, and with a dedicated and flexible structure. As it supports sending kafka messages, an [Azure Event Hub for Kafka](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-for-kafka-ecosystem-overview) is used as the endpoint to which data is sent to.

This repository includes a [modified version of Pepper-Box](./pepper-box) with changes required to support our scenario:

- Updated support Kafka version 1.0.1
- Support jmeter version 3.0
- A custom function to generate data with a specific structure
- A data-schema JSON file to describe message structure and some metadata

## Data Structure

Each machine in our factory includes many different sensors that operate at different frequencies. Each sensor will output a reading with a timestamp and value at a frequency suitable for it.
Having sensors and/or machines send their data directly to the cloud was impractical because of the bandwidth requirements so we had to combine data-points into bigger messages while reducing the overall message size.

[GEN_MANUFACTORING_SENSORS_DATA](./pepper-box/src/main/java/com/gslab/pepper/input/CustomFunctions.java) is a function written according to the Pepper-Box spec that generates json messages in the format we need.

### Sample Message

 ```json
{
	"work-order-id": "1547384013635",
	"machine-id": "M1-720",
	"data": [
		{
			"name": "sensor-10",
			"timestamp": 1547384017043,
			"values": [
				0.761147193070, 0.55351074482025, 0.79442620950237, 0.20240347169579, 0.30507880561728
				],
			"time-delta": [
				768, 758, 729, 368, 854
				]
		},
		{
			"name": "sensor-20",
			"timestamp": 1547384017043,
			"values": [
                0.761147070779, 0.55351074425908, 0.79440950271237, 0.20240347169579, 0.30503197861728
				],
			"time-delta": [
				768, 758, 729, 368, 854
				]
		},
		{
			"name": "sensor-30",
			"timestamp": 1547384017043,
			"values": [
				0.761147170779, 0.55351074485908, 0.79442950271237, 0.20249460169579, 0.30503190561728
				],
			"time-delta": [
				768, 758, 729, 368, 854
				]
		}
		]
}
```

Each `data` object includes a sensor, base `timestamp` and two arrays with the `time-delta` and `values`. This structure can save many characters since we don't repeat the base timestamps as well as other common fields for each `value`.
In certain scenarios this _compact_ format can have a big effect and reduce payloads considerably. However, as will be discussed later, this format presents a challenge when coming to query the information.

## Run to generate data

To simplify the data generation process, we packed the Pepper-Box with its dependencies in a container, and pushed it to [Docker Hub](https://hub.docker.com/).

There are 2 ways to run the data generator:

### Option 1: Using Docker

If you have Docker installed on your machine, you can use it to run the data generator:

1. Fetch the latest version of our Pepper-Box image from Docker Hub:

    ```bash
        docker pull rabeeaas/pepperbox:latest
    ```

1. Run the image with the following command:

    ```bash
        docker run --name=pepperbox -e EH_ENDPOINT="<EventHubEndpoint>" -e EH_NS_CONNECTION_STR="<EventHubNamespaceConnectionStr>" -e EH_NAME="<EventHubName>" rabeeaas/pepperbox:latest
    ```

    `Environment Variables` mentioned above, and other optional ones, will be described in next section.

### Option 2: Using Azure Container Instances

With Azure Container Instances it is possible to run Docker images easily on the cloud without anything installed on your machine. The following steps do so by using Azure CLI:

1. If you don't have a Resource Group, create one in your preferred location (in this example we used West Europe):

    ```bash
        az group create --name <myResourceGroup> --location westeurope
    ```

1. Run the container with the following command:

    ```bash
        az container create --resource-group <myResourceGroup> --name pepperbox --image rabeeaas/pepperbox:latest --environment-variables EH_ENDPOINT="<EventHubEndpoint>" EH_NS_CONNECTION_STR="<EventHubNamespaceConnectionStr>" EH_NAME="<EventHubName>" --memory 5 --cpu 4
    ```

    `Environment Variables` mentioned above, and other optional ones, will be described in next section.

1. Check the status of the instance with the following command:

    ```bash
        az container show --resource-group <myResourceGroup> --name pepperbox --out table
    ```

### Environment Variables

To run the data generator, there are several required arguments to be passed as environment variables when running the container:

- `EH_ENDPOINT`*: (**required**) The Event Hub endpoint consists of the Event Hub FQDN and the port number in this format: `FQDN`:`Port`
  - Example: `EH_NamespaceName`.servicebus.windows.net:9093
- `EH_NS_CONNECTION_STR`*: (**required**) The Event Hub Namespace connection string.
- `EH_NAME`*: (**required**) The Event Hub name.
- `MACHINE_ID`: A custom ID to be ingested with the sensors data into Kusto (*default: 'M-100'*)
- `MIN_NUM_OF_SENSORS`: Minimum number of sensors to be sent in each message (*default: 5*)
- `MAX_NUM_OF_SENSORS`: Maximum number of sensors to be sent in each message (*default: 5*)
- `MIN_LENGTH_OF_VALUES`: Minimum length of time-delta/values arrays for each sensor in the message (*default: 50*)
- `MAX_LENGTH_OF_VALUES`: Maximum length of time-delta/values arrays for each sensor in the message (*default: 50*)
- `TEST_DURATION`: The total time in seconds for the Pepper-Box to run (*default: 300*)
- `NUM_PRODUCERS`: The number of instances to send messages (*default: 10*)
- `THROUGHPUT_PER_PRODUCER`: The throughput of each producer (*default: 200*)

**More information about Event Hub values can be found [here](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-get-connection-string)*

## Validate

While the docker image is running you should validate that the data reaches the table on Kusto. You can do this with the following query:

```kusto
landing
| order by ingestion_time() desc
| take 100
```

This query will fetch the last 100 rows that got ingested into the `landing` table.

## Next Steps

When you have completed this section, go on to the next one: [Process Data](../processing/README.md)
