package com.example.logic_university;

import org.json.JSONObject;

public class Command2 {
    protected AsyncTaskPOST.IServerResponse2 callback;
    protected String url;
    protected JSONObject data;

    public Command2(AsyncTaskPOST.IServerResponse2 callback, String url, JSONObject data) {
        this.callback = callback;
        this.url=url;
        this.data = data;
    }
}
