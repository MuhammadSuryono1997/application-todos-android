package com.yono.mytodos.api.response;

import com.google.gson.annotations.SerializedName;
import com.yono.mytodos.api.models.DataTodos;

import java.util.List;

public class TodosResponse {
    @SerializedName("status")
    boolean statusTodos;

    @SerializedName("message")
    private String messageTodos;

    @SerializedName("data")
    private List<DataTodos> dataTodos;

    public boolean isStatusTodos() {
        return statusTodos;
    }

    public void setStatusTodos(boolean statusTodos) {
        this.statusTodos = statusTodos;
    }

    public String getMessageTodos() {
        return messageTodos;
    }

    public void setMessageTodos(String messageTodos) {
        this.messageTodos = messageTodos;
    }

    public List<DataTodos> getDataTodos() {
        return dataTodos;
    }

    public void setDataTodos(List<DataTodos> dataTodos) {
        this.dataTodos = dataTodos;
    }
}
