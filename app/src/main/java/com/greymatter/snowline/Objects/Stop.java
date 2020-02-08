package com.greymatter.snowline.Objects;

public class Stop {
    private String number, name, direction;
    private Street street, crossStreet;
    private Centre centre;
    public Stop(){}

    public String getNumber() {
        return number;
    }

    public Stop setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public Stop setName(String name) {
        this.name = name;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public Stop setDirection(String direction) {
        this.direction = direction;
        return this;
    }
    public Street getStreet() {
        return street;
    }

    public Stop setStreet(Street street) {
        this.street = street;
        return this;
    }

    public Street getCrossStreet() {
        return crossStreet;
    }

    public Stop setCrossStreet(Street crossStreet) {
        this.crossStreet = crossStreet;
        return this;
    }

    public Centre getCentre() {
        return centre;
    }

    public Stop setCentre(Centre centre) {
        this.centre = centre;
        return this;
    }

    public String toString(){
        return "{ "+ this.name + ", "+ this.number + " }";
    }

}
