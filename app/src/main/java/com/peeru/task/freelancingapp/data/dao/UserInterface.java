package com.peeru.task.freelancingapp.data.dao;

public interface UserInterface {
    public String getFirstName();

    public void setFirstName(String firstName);

    public String getLastName();

    public void setLastName(String lastName);

    public String getEmail();

    public void setEmail(String email);

    public String getUserRole();

    public void setUserRole(String userRole);

    public String getDateCreated();

    public void setDateCreated(String dateCreated);

    public String getLastUpdate();

    public void setLastUpdate(String lasteUpdate);

    public String getPassword();

    public void setPassword(String password);

}
