package com.example.earthquake_inspector_app;

public class User {
    //Class values
    private String username;
    private String password;
    private String PM_id;

    //Default Constructor
    public User(){}

    //Custom Constructor
    public User(String u, String p, String PM){
        this.username=u;
        this.password=p;
        this.PM_id=PM;
    }

    //Getters-Setters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getPM_id() {
        return PM_id;
    }

}
