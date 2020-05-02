package com.example.logic_university;

import android.os.Parcelable;

import java.io.Serializable;

public class UserModel implements  Serializable
{
    public int Empid;
    public String Empname;

    public UserModel(int id, String name) {
        this.Empid = id;

        this.Empname = name;
    }
    public UserModel() {

    }


    public int getId() {
        return Empid;
    }

    public void setId(int id) {
        this.Empid = id;
    }

    public String getName() {
        return Empname;
    }

    public void setName(String name) {
        this.Empname = name;
    }
}
