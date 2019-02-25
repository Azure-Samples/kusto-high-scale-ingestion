package com.gslab.pepper.input;

import java.util.Random;

public class SensorsData {

    SensorsData(String name, int dataListSize, Random random) {
        setSensorData(name, dataListSize, random);
    }

    private String name;
    private long timestamp;
    private int[] timeDelta;
    private double[] values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int[] getTimeDelta() {
        return timeDelta;
    }

    public void setTimeDelta(int[] timeDelta) {
        this.timeDelta = timeDelta;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    private void setSensorData(String name, int dataListSize, Random random) {
        this.name = name;
        this.timestamp = System.currentTimeMillis() * 1000 + random.nextInt(1000);
        this.timeDelta = new int[dataListSize];
        this.values = new double[dataListSize];

        for (int i = 0; i < dataListSize; i++) {
            this.timeDelta[i] = random.nextInt(1000) * 1000;
            this.values[i] = random.nextDouble();
        }
    }
}
