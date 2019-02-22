package com.nitinsir.sensordata;

public class DataStorage {
    String statement;
    int r_color;
    int g_color;
    int b_color;
    String image;

    public DataStorage(String statement, int r_color, int g_color, int b_color, String image) {
        this.statement = statement;
        this.r_color = r_color;
        this.g_color = g_color;
        this.b_color = b_color;
        this.image = image;
    }

    public String getStatement() {
        return statement;
    }

    public int getR_color() {
        return r_color;
    }

    public int getG_color() {
        return g_color;
    }

    public int getB_color() {
        return b_color;
    }

    public String getImage() {
        return image;
    }

}
