import argparse
from azure.kusto.data.request import KustoClient, KustoConnectionStringBuilder

arg_parser = argparse.ArgumentParser()
arg_parser.add_argument('--cluster', '-c', help="Kusto cluster URL", type=str, required=True)
arg_parser.add_argument('--database', '-db', help="Kusto database", type=str, required=True)
args = arg_parser.parse_args()

cluster = args.cluster
database = args.database

def send_kusto_command_from_file(client, database, file_path):
    """ Reads a Kusto command from a file, and sends the command to Kusto and returns
        the results from the response
    """
    if(not client or not database.strip() or not file_path.strip()):
        raise ValueError("All arguments of this function are mandatory")

    kusto_file = open(file_path, 'r')
    command = kusto_file.read().replace('\n', ' ')
    response = client.execute_mgmt(database, command)

    return response.primary_results[0]

# create a connection string with a device code (=interactive) authentication
kcsb = KustoConnectionStringBuilder.with_aad_device_authentication(cluster)

# create the client and do the actual authentication
client = KustoClient(kcsb)

# run the command to create the target table
response = send_kusto_command_from_file(client, database, './table-target-create.csl')
print("Created target table. Response = ", response)

# run the command to create the function of data parsing
response = send_kusto_command_from_file(client, database, './function-source-data-parsing.csl')
print("Created parsing function. Response = ", response)

# run the command to create the update policy to parse the source data into the target table
response = send_kusto_command_from_file(client, database, './update-policy-target.csl')
print("Created update policy. Response = ", response)
