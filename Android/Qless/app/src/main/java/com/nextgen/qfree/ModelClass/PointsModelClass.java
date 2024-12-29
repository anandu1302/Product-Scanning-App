package com.nextgen.qfree.ModelClass;

public class PointsModelClass {

    String id;
    String points;

    public PointsModelClass(String id,String points) {
        this.id = id;
        this.points = points;
    }

    public String getId(){
        return id;
    }

    public String getPoints(){
        return points;
    }

}
