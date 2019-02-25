import argparse
from azure.kusto.data.request import KustoClient, KustoConnectionStringBuilder

arg_parser = argparse.ArgumentParser()
arg_parser.add_argument('--cluster', '-cluster', help="Kusto cluster URL", type=str, required=True)
arg_parser.add_argument('--database', '-db', help="Kusto database", type=str, required=True)
args = arg_parser.parse_args()

cluster = args.cluster
database = args.database

# define the command to create our first table and mapping in the database
LANDING_TABLE_CMD = ".create-merge table landing (rawdata: dynamic)"
LANDING_TABLE_MAPPING_CMD = ".create table landing ingestion json mapping \"raw_mapping\" '[{\"column\":\"rawdata\",\"path\":\"$\"}]'"

# create a connection string with a device code (=interactive) authentication
kcsb = KustoConnectionStringBuilder.with_aad_device_authentication(cluster)

# create the client and do the actual authentication
client = KustoClient(kcsb)

# run the command to create the table
response = client.execute_mgmt(database, LANDING_TABLE_CMD)

# run the command to create the table mapping
response = client.execute_mgmt(database, LANDING_TABLE_MAPPING_CMD)