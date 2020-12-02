package com.morningstar.barcodevisionapi;

import java.io.Serializable;
import java.util.ArrayList;

public class DataWrapper implements Serializable {

    private ArrayList<Batch> batches;

    public DataWrapper(ArrayList<Batch> batches) {
        this.batches = batches;
    }

    public ArrayList<Batch> getBatches() {
        return this.batches;
    }

}