package com.yono.mytodos.api.service;


import com.yono.mytodos.api.models.DataTodos;
import com.yono.mytodos.api.models.DeleteModels;
import com.yono.mytodos.api.response.TodosResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TodosService {
    @GET("v1/todos")
    Call<TodosResponse> getAllTodos();

    @Headers("Content-Type: application/json")
    @POST("v1/todos")
    Call<List<TodosResponse>> addTodos(@Body DataTodos dataTodos);

    @Headers("Content-Type: application/json")
    @PUT("v1/todos/{id}")
    Call<DeleteModels> updateTodos(@Path("id") int id, @Body DataTodos dataTodos);

    @DELETE("v1/todos/{id}")
    Call<DeleteModels> deleteTodo(@Path("id") int id);

}
