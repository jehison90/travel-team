package com.team.travel.travelteam.data.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jehison on 10/05/2016.
 */
public class Position implements Serializable {

    private PositionPk positionPk;

    private double lastX;

    private double lastY;

    private Date lastUpdated;

    private boolean active;

    private boolean normalPosition;

    private Route route;

    private User user;

    public PositionPk getPositionPk() {
        return positionPk;
    }

    public void setPositionPk(PositionPk positionPk) {
        this.positionPk = positionPk;
    }

    public double getLastX() {
        return lastX;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isNormalPosition() {
        return normalPosition;
    }

    public void setNormalPosition(boolean normalPosition) {
        this.normalPosition = normalPosition;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}