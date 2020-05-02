package com.example.logic_university;

import org.json.JSONObject;

public class Command3 {
    protected AsyncTaskGET.IServerResponse3 callback;
    protected String url;

    public Command3(AsyncTaskGET.IServerResponse3 callback, String url) {
        this.callback = callback;
        this.url=url;
    }
}
