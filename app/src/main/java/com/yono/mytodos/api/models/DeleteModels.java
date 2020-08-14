package com.yono.mytodos.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeleteModels {
    @SerializedName("status")
    boolean statusTodos;

    @SerializedName("message")
    private String messageTodos;

    @SerializedName("data")
    private String data;
}
