package com.peeru.task.freelancingapp.data.model;


import java.util.HashMap;

public class Task {
    private String taskTitle;
    private String taskDescription;
    private String taskOwnerId;
    private String taskServicerId;
    private String workerRating;
    private String userRating;
    private Double lat;
    private Double lon;
    private Double distance;
    private String taskStatus;
    private String createdDate;
    private String modifyDate;
    private HashMap<String, String> taskStatusHistory = new HashMap<String, String>();

    public Task() {
    }

    public Task(String userName, String title, String type, Double lat, Double lon, String dateTime) {
        taskTitle = title;
        taskOwnerId = userName;
        taskDescription = type;
        this.lat = lat;
        this.lon = lon;
        workerRating = "N/A";
        userRating = "N/A";
        taskStatus = "Open";
        createdDate = dateTime;
        modifyDate = dateTime;
        taskStatusHistory.put("Open", dateTime);
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskOwnerId() {
        return taskOwnerId;
    }

    public void setTaskOwnerId(String taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
    }

    public String getTaskServicerId() {
        return taskServicerId;
    }

    public void setTaskServicerId(String taskServicerId) {
        this.taskServicerId = taskServicerId;
    }

    public String getWorkerRating() {
        return workerRating;
    }

    public void setWorkerRating(String workerRating) {
        this.workerRating = workerRating;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public HashMap<String, String> getTaskStatusHistory() {
        return taskStatusHistory;
    }

    public void setTaskStatusHistory(HashMap<String, String> taskStatusHistory) {
        this.taskStatusHistory = taskStatusHistory;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskTitle='" + taskTitle + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskOwnerId='" + taskOwnerId + '\'' +
                ", taskServicerId='" + taskServicerId + '\'' +
                ", workerRating='" + workerRating + '\'' +
                ", userRating='" + userRating + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", taskStatus='" + taskStatus + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", modifyDate='" + modifyDate + '\'' +
                ", taskStatusHistory=" + taskStatusHistory +
                '}';
    }
}
