package com.morningstar.barcodevisionapi;

import java.util.Arrays;
import java.util.Objects;


public class Container {
    private int batchID;
    private String barcode;
    private String date;
    private int row;
    private int section;

    //Default Constructor
    public Container(){
        batchID =0;
        row = 0;
        section = 0;
        barcode = "";
        date = "";
    }

    public Container (int batchID, String barcode, String date, int row, int section){
        this.batchID =  batchID;
        this.row = row;
        this.section = section;
        this.barcode = barcode;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Container{" +
                "batchID=" + batchID +
                ", barcode='" + barcode + '\'' +
                ", date=" + date +
                ", row=" + row +
                ", section=" + section +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Container container = (Container) o;
        return batchID == container.batchID &&
                row == container.row &&
                section == container.section &&
                Objects.equals(barcode, container.barcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(batchID, barcode, row, section, date);
    }

    //Setters
    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void setDate(String date) { this.date = date; }

    //Getters
    public int getBatchID() {
        return batchID;
    }

    public int getRow() {
        return row;
    }

    public int getSection() {
        return section;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDate() { return date; }
}
