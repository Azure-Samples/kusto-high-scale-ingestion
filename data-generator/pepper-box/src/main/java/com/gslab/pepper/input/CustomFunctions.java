package com.gslab.pepper.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CustomFunctions allows users to write custom functions and then it can be used in template.
 *
 * @Author Satish Bhor<satish.bhor@gslab.com>, Nachiket Kate <nachiket.kate@gslab.com>
 * @Version 1.0
 * @since 01/03/2017
 */
public class CustomFunctions {
    private static final Logger log = Logger.getLogger(FieldDataFunctions.class.getName());
    private static Random random = new Random();
    private static ObjectMapper mapper = new ObjectMapper();
    private static int[] sensorsRanges = { 20, 100, 200 };
    private static double[] dataSizeMultiplier = { 2.0, 1.0, 0.5 };
    
    public static String GEN_MANUFACTORING_SENSORS_DATA(int sensorsCountMin, int sensorsCountMax, int dataArrayLengthLow,
            int dataArrayLengthHigh) {
        sensorsCountMin = Math.max(0, sensorsCountMin);
        sensorsCountMax = Math.min(sensorsCountMax, 50);
        dataArrayLengthLow = Math.max(0, dataArrayLengthLow);
        dataArrayLengthHigh = Math.min(dataArrayLengthHigh, 100);

        if (sensorsCountMin > sensorsCountMax) {
            throw new IllegalArgumentException("sensorsCountMin > sensorsCountMax");
        }
        if (dataArrayLengthLow > dataArrayLengthHigh) {
            throw new IllegalArgumentException("dataArrayLengthLow > dataArrayLengthHigh");
        }

        int sesnsorsListSize = random.nextInt(1 + sensorsCountMax - sensorsCountMin) + sensorsCountMin;
        ArrayList<SensorsData> dataList = new ArrayList<>(sesnsorsListSize);
        ArrayList<String> names = new ArrayList<>(sesnsorsListSize);

        for (int i = 0; i < sesnsorsListSize; i++) {
            //Choose a random group:
            int sensorGroup = random.nextInt(sensorsRanges.length);
            int dataListSize = getDataListSize(sensorGroup, dataArrayLengthLow, dataArrayLengthHigh);
            String sensorName = genSensorName(sensorGroup, names);
            dataList.add(new SensorsData(sensorName, dataListSize, random));
        }
        
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE, "Failed to map object to JSON string", e);
            return null;
        }
    }

    private static int getDataListSize(int sensorGroup, int dataArrayLengthLow, int dataArrayLengthHigh) {
        // Validation
        if (dataArrayLengthLow > dataArrayLengthHigh) {
            throw new IllegalArgumentException("dataArrayLengthLow > dataArrayLengthHigh");
        }

        // Here we set the list size to dataArrayLengthLow. 
        // This will be correct if dataArrayLengthLow == dataArrayLengthHigh, and then we don't need to use "random"
        int dataListSize = dataArrayLengthLow;
		if(dataArrayLengthLow != dataArrayLengthHigh){
            // If dataArrayLengthLow != dataArrayLengthHigh, then choose a random integer between dataArrayLengthLow
            // and dataArrayLengthHigh
		    dataListSize = random.nextInt(1 + dataArrayLengthHigh - dataArrayLengthLow) + dataArrayLengthLow;
            
            // Mutiple the integer by the relevant multiplier for this sensorGroup. This will modify the 
            // list size according to the current sensorGroup. 
            // For example: if we get 100, and we are in sensorGroup 0, then the integer will be multiplied by
            // 2 and become 200. This gives more results for sensors from this group.
            dataListSize = (int) Math.ceil(dataListSize * dataSizeMultiplier[sensorGroup]);

            // Now make sure the list size is still between dataArrayLengthLow and dataArrayLengthHigh
            dataListSize = Math.max(dataArrayLengthLow, dataListSize);
            dataListSize = Math.min(dataArrayLengthHigh, dataListSize);
        }
        return dataListSize;
    }

    private static String genSensorName(int sensorGroup, ArrayList<String> namesList) {
        String sensorName = "sensor-" + random.nextInt(sensorsRanges[sensorGroup]);
        // Make sure that this sensore is not existing yet in the list:
        while (namesList.contains(sensorName)) {
            sensorName = "sensor-" + random.nextInt(sensorsRanges[sensorGroup]);
            }
        namesList.add(sensorName);
        return sensorName;
    }
}
