package com.mehboob.myshadi.json;

public class States {

    private String code;

    private String country;
    private int id;
    private String name;

    public States() {
    }

    public States(String code, String country, int id, String name) {
        this.code = code;
        this.country = country;
        this.id = id;
        this.name = name;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
