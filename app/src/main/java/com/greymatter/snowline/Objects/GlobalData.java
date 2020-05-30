package com.greymatter.snowline.Objects;

public class GlobalData {
    private int tripsTableId, shapesTableId;
    private boolean tripsTableLoaded, shapesTableLoaded;

    public GlobalData() {}

    public int getTripsTableId() {
        return tripsTableId;
    }

    public void setTripsTableId(int tripsTableId) {
        this.tripsTableId = tripsTableId;
    }

    public int getShapesTableId() {
        return shapesTableId;
    }

    public void setShapesTableId(int shapesTableId) {
        this.shapesTableId = shapesTableId;
    }

    public boolean isTripsTableLoaded() {
        return tripsTableLoaded;
    }

    public void setTripsTableLoaded(boolean tripsTableLoaded) {
        this.tripsTableLoaded = tripsTableLoaded;
    }

    public boolean isShapesTableLoaded() {
        return shapesTableLoaded;
    }

    public void setShapesTableLoaded(boolean shapesTableLoaded) {
        this.shapesTableLoaded = shapesTableLoaded;
    }
}
