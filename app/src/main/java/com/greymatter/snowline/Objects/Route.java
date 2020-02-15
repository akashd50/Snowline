package com.greymatter.snowline.Objects;

import java.util.ArrayList;

public class Route {
    private String key, name, number, coverage;

    public Route(String key, String number, String name){
        this.key = key;
        this.name = name;
        this.number = number;
    }
    public Route(){}

    public String getNumber() {
        return number;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString(){
        return this.name;
    }
}
