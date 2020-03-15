package com.greymatter.snowline.Objects;

import com.greymatter.snowline.Data.entities.StreetEntity;

public class Street {
    private String key, name, type;
    public Street(){}
    public Street(String key, String name, String type){
        this.key = key; this.name = name; this.type = type;
    }
    public Street(StreetEntity streetEntity){
        //key = streetEntity.key+"";
        name = streetEntity.streetName;
        type = streetEntity.streetType;
    }


    public String getKey() {
        return key;
    }

    public Street setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public Street setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Street setType(String type) {
        this.type = type;
        return this;
    }
}
