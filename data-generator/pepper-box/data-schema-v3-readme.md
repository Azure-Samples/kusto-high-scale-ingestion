Here is a short description about the usage of our custom function:

GEN_MANUFACTORING_SENSORS_DATA(minNumOfSensors,maxNumOfSensors, dataArrayLengthLow, dataArrayLengthHigh)

Where:
    - minNumOfSensors: Minimum number of sensors to be sent.
    - maxNumOfSensors: Maximum number of sensors to be sent.
    - dataArrayLengthLow: Minimum length of timeDelta/values arrays.
    - dataArrayLengthHigh: Maximum length of timeDelta/values arrays.

Example output json of this function:
{
	{
		"name": "sensor-1177",
		"timestamp": 1548143038389846,
		"timeDelta": [779000, 788000, 679000, 941000, 590000],
		"values": [0.056965742240631112, 0.328216232519668, 0.38893118532176019, 0.20601967215021388, 0.025499201566661189]
	},
	{
		"name": "sensor-11",
		"timestamp": 1548143038389846,
		"timeDelta": [532000, 962000, 139000],
		"values": [0.91582749805630626, 0.27739169826196652, 0.45371735643145439]
	},
	{
		"name": "sensor-1555",
		"timestamp": 1548143038389846,
		"timeDelta": [884000, 497000, 160000, 799000, 967000, 326000, 746000],
		"values": [0.510056230128417, 0.075336590848456453, 0.67038193448182437, 0.639223307138406, 0.56519628626034868, 0.60813657292551293, 0.19501450860258085]
	}
}
