package com.team.travel.travelteam.data.entities;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Jehison on 09/05/2016.
 */

public class Route implements Serializable {

    private Integer id;

    private double initialX;

    private double initialY;

    private double finalX;

    private double finalY;

    private Date initialDateTime;

    private Date finalDateTime;

    private String initialPlaceDescription;

    private String finalPlaceDescription;

    private List<Position> positions;

    public Route() {
    }

    public Route(Integer id, double initialX, double initialY, double finalX, double finalY, Date initialDateTime, Date finalDateTime, String initialPlaceDescription, String finalPlaceDescription) {
        this.id = id;
        this.initialX = initialX;
        this.initialY = initialY;
        this.finalX = finalX;
        this.finalY = finalY;
        this.initialDateTime = initialDateTime;
        this.finalDateTime = finalDateTime;
        this.initialPlaceDescription = initialPlaceDescription;
        this.finalPlaceDescription = finalPlaceDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getInitialX() {
        return initialX;
    }

    public void setInitialX(double initialX) {
        this.initialX = initialX;
    }

    public double getInitialY() {
        return initialY;
    }

    public void setInitialY(double initialY) {
        this.initialY = initialY;
    }

    public double getFinalX() {
        return finalX;
    }

    public void setFinalX(double finalX) {
        this.finalX = finalX;
    }

    public double getFinalY() {
        return finalY;
    }

    public void setFinalY(double finalY) {
        this.finalY = finalY;
    }

    public Date getInitialDateTime() {
        return initialDateTime;
    }

    public void setInitialDateTime(Date initialDateTime) {
        this.initialDateTime = initialDateTime;
    }

    public Date getFinalDateTime() {
        return finalDateTime;
    }

    public void setFinalDateTime(Date finalDateTime) {
        this.finalDateTime = finalDateTime;
    }

    public String getInitialPlaceDescription() {
        return initialPlaceDescription;
    }

    public void setInitialPlaceDescription(String initialPlaceDescription) {
        this.initialPlaceDescription = initialPlaceDescription;
    }

    public String getFinalPlaceDescription() {
        return finalPlaceDescription;
    }

    public void setFinalPlaceDescription(String finalPlaceDescription) {
        this.finalPlaceDescription = finalPlaceDescription;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}