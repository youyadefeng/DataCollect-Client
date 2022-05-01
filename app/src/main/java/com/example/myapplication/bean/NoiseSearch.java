package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.List;

public class NoiseSearch implements Serializable {
    int noiseDb;

    public NoiseSearch(int noiseDb) {
        this.noiseDb = noiseDb;
    }

    public int getNoiseDb() {
        return noiseDb;
    }

    public void setNoiseDb(int noiseDb) {
        this.noiseDb = noiseDb;
    }

    public static NoiseSearch BuildData(List<String> prams)
    {
        int db = Integer.parseInt(prams.get(0));

        NoiseSearch object = new NoiseSearch(db);
        return object;
    }
}
