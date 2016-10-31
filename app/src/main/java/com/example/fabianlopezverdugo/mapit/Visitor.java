package com.example.fabianlopezverdugo.mapit;

/**
 * Created by fabianlopezverdugo on 10/30/16.
 */

public class Visitor
{
    private long id;
    private String name;
    private String description;
    private String picture;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public  void setName(String name){
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String pass){
        this.description = pass;
    }

    @Override
    public String toString(){
        return name+"|"+description+"|"+picture;
    }
}
