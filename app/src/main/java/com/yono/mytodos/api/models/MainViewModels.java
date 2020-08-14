package com.yono.mytodos.api.models;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.yono.mytodos.MainActivity;
import com.yono.mytodos.api.ApiHelper;
import com.yono.mytodos.api.client.Client;
import com.yono.mytodos.api.response.TodosResponse;
import com.yono.mytodos.api.service.TodosService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModels extends ViewModel {
    MutableLiveData<TodosResponse> todosList;
    Client client;
    TodosService service;
    LiveData<List<DataTodos>> dataTodos;
    private MutableLiveData<String> query = new MutableLiveData<>("%");

    public MainViewModels(){
        client = new Client();
        todosList = new MutableLiveData<>();
        getAllTodos();
    }

    public MutableLiveData<TodosResponse> getTodos(){
        return todosList;
    }

    public LiveData<List<DataTodos>> getTodo(){
        return dataTodos;
    }

    public void filter(String s) {
        String query = TextUtils.isEmpty(s) ? "%" : "%" + s + "%";
        this.query.postValue(query);
    }

    private void getAllTodos() {
        service = client.Client(TodosService.class, ApiHelper.BASE_URL);

        service.getAllTodos().enqueue(new Callback<TodosResponse>() {
            @Override
            public void onResponse(Call<TodosResponse> call, Response<TodosResponse> response) {
                todosList.setValue(response.body());
                String jf = new Gson().toJson(response.body());
                Log.d("Get todos", "Response : "+jf);
            }

            @Override
            public void onFailure(Call<TodosResponse> call, Throwable t) {

            }
        });
    }

    public void deleteTodos(int idTodos){
        service.deleteTodo(idTodos).enqueue(new Callback<DeleteModels>() {
            @Override
            public void onResponse(Call<DeleteModels> call, Response<DeleteModels> response) {
                String st = new Gson().toJson(response.body());
                Log.d("Status Delete ", st);
            }

            @Override
            public void onFailure(Call<DeleteModels> call, Throwable t) {
                Log.d("Status Update ", t.getMessage());
            }
        });
    }

    public void updateTodos(int id, DataTodos dataTodos){
        service.updateTodos(id, dataTodos);
    }

    public void insertTodos(DataTodos dataTodos){
        service.addTodos(dataTodos);
    }
}
