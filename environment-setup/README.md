# Azure Environment Setup

This document will guide you through the steps needed to provision the solution on Azure.

## Step 1 - Create a Resource Group

Create a new resource group by using the Azure Portal or with the following AzureCLI command:

```bash
az group create --name "$resourceGroupName" --location "$location"
```

## Step 2 - Provision Azure Resources

We need to have a working kusto cluster, database and an Eventhubs.

You can create them by using directly on the Azure Portal or by using [azure-env-setup.json](azure-env-setup.json) - an Azure Resource Manager (ARM) template that will provision a Kusto cluster and an Eventhubs.

The command below is an example of how to deploy the template while setting some of the parameters used inside it, you can view more options by looking into the file itself.

```bash
az group deployment create --name "env-setup" --template-file azure-env-setup.json --resource-group "$resourceGroupName" --parameters location="$location" kusto_cluster_name="$kustoClusterName" eventhubs_namespace_name="$eventhubsNamespaceName" eventhubs_hub_name="$eventhubsHubName" --query properties.outputs.clusterUri.value
```

Note that the command will output the URL for your kusto cluster which you will need in Step 3.

## Step 3 - Create Kusto Table & Mapping

After we have our cluster up and running with a new database we can go ahead and create the objects within it. We need a table to store the data on, as well as a mapping object which will direct Kusto on how to translate the incoming payload from Eventhubs to the table columns.

### Option 1: Using Azure Portal

1. On the Azure Portal, choose your Kusto cluster and then your database
1. Click on the `Query` line
1. Create the table that will contain the entire message payload in one column:

    ```kusto
    .create-merge table landing (rawdata: dynamic)
    ```

1. Create a mapping to define that the incoming message payload should be written as-is to the rawdata column:

    ```kusto
    .create table landing ingestion json mapping "raw_mapping" '[{"column":"rawdata","path":"$"}]'
    ```

### Option 2: Using Python

The following command uses the [kusto python sdk](https://docs.microsoft.com/en-us/azure/kusto/api/python/kusto-python-client-library) to accomplish this task. It runs the [adx_objects_setup.py](adx_objects_setup.py) script that holds the right commands

```bash
python adx_objects_setup.py --cluster "$kustoClusterURL" --database "$kustoDatabaseName"
```

## Step 4 - Create Data Ingestion

Data ingestion is where we connect Kusto to Eventhubs and start ingest the data.

As before, you can define this on the Azure Portal UI by reading this [documentation](https://docs.microsoft.com/en-us/azure/data-explorer/ingest-data-event-hub#connect-to-the-event-hub), or by deploying [data-ingestion.json](data-ingestion.json) with the following command:

```bash
az group deployment create --name "dataingest-setup" --template-file data-ingestion.json --resource-group "$resourceGroupName" --parameters location="$location" kusto_cluster_name="$kustoClusterName" kusto_database_name="$kustoDatabaseName" eventhubs_namespace_name="$eventhubsNamespaceName" eventhubs_hub_name="$eventhubsHubName"
```

## Next Steps

When you have completed this section, go on to the next one: [Generate Data](../data-generator/README.md)