package com.yono.mytodos.api.models;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.annotations.SerializedName;
import com.yono.mytodos.R;

public class DataTodos {
    @SerializedName("id")
    private int idTodos;

    @SerializedName("task")
    private String taskTodos;

    @SerializedName("createdAt")
    private String createdAtTodos;

    @SerializedName("updatedAt")
    private String updatedAtTodos;

    @SerializedName("status")
    boolean statusTodos;

    public int getIdTodos() {
        return idTodos;
    }

    public void setIdTodos(int idTodos) {
        this.idTodos = idTodos;
    }

    public String getTaskTodos() {
        return taskTodos;
    }

    public void setTaskTodos(String taskTodos) {
        this.taskTodos = taskTodos;
    }

    public String getCreatedAtTodos() {
        return createdAtTodos;
    }

    public void setCreatedAtTodos(String createdAtTodos) {
        this.createdAtTodos = createdAtTodos;
    }

    public String getUpdatedAtTodos() {
        return updatedAtTodos;
    }

    public void setUpdatedAtTodos(String updatedAtTodos) {
        this.updatedAtTodos = updatedAtTodos;
    }

    public boolean isStatusTodos() {
        return statusTodos;
    }

    public void setStatusTodos(boolean statusTodos) {
        this.statusTodos = statusTodos;
    }

    @BindingAdapter("status")
    public static void setStatusImage(ImageView image, Boolean statusTodos){
        Log.d("image", "data"+statusTodos);
        if (statusTodos){
            image.setImageResource(R.drawable.ic_check);
        }else{
            image.setImageResource(R.drawable.ic_close);
        }
    }
}
