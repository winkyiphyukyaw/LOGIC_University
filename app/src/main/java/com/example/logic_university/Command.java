package com.example.logic_university;

import org.json.JSONObject;

public class Command {
    protected AsyncTaskToServer.IServerResponse1 callback;
    protected String url;
    protected JSONObject data;

    public Command(AsyncTaskToServer.IServerResponse1 callback, String url, JSONObject data) {
        this.callback = callback;
        this.url=url;
        this.data = data;
    }
}

