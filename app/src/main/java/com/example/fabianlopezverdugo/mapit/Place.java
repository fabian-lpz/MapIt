package com.example.fabianlopezverdugo.mapit;

/**
 * Created by fabianlopezverdugo on 10/30/16.
 */

public class Place
{
    private long id;
    private String name;
    private double latitude;
    private double longitude;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public  void setName(String userName){
        this.name = userName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString(){
        return name+" // "+latitude+" //"+longitude;
    }

}
