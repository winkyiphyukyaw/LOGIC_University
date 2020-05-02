package com.example.logic_university;

public class ListItem {
    private String depname;
    private String ID;

    public ListItem(String depname, String ID) {
        this.depname = depname;
        this.ID = ID;
    }

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
