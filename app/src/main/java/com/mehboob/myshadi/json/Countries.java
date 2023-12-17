package com.mehboob.myshadi.json;

public class Countries {


    private String code;
    private int id;
    private String iso_3;
    private String name;
    private String namecap;


    public Countries(String code, int id, String iso_3, String name, String namecap) {
        this.code = code;
        this.id = id;
        this.iso_3 = iso_3;
        this.name = name;
        this.namecap = namecap;
    }

    public Countries() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIso_3() {
        return iso_3;
    }

    public void setIso_3(String iso_3) {
        this.iso_3 = iso_3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamecap() {
        return namecap;
    }

    public void setNamecap(String namecap) {
        this.namecap = namecap;
    }
}
