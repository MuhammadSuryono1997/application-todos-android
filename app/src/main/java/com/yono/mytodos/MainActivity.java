package com.yono.mytodos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.yono.mytodos.api.ApiHelper;
import com.yono.mytodos.api.adapter.TodosAdapter;
import com.yono.mytodos.api.client.Client;
import com.yono.mytodos.api.models.DataTodos;
import com.yono.mytodos.api.models.DeleteModels;
import com.yono.mytodos.api.models.MainViewModels;
import com.yono.mytodos.api.response.TodosResponse;
import com.yono.mytodos.api.service.TodosService;
import com.yono.mytodos.databinding.ActivityMainBinding;
import com.yono.mytodos.databinding.FormTaskBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MainViewModels viewModels;
    ActivityMainBinding binding;
    TodosAdapter todosAdapter;
    ProgressBar pbLoading;


    TextInputEditText textInputLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        todosAdapter = new TodosAdapter();
        textInputLayout = findViewById(R.id.etTaskName);

        showLoading(true);
        getAllDataTodos();
        listenerAction();

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                todosAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void listenerAction(){
        todosAdapter.setListener(new TodosAdapter.TodosListener() {
            @Override
            public void onUpdate(DataTodos dataTodos) {
                FormTaskBinding formTaskBinding;
                formTaskBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.form_task);

                if (dataTodos!=null){
                    formTaskBinding.etTaskName.setText(dataTodos.getTaskTodos());
                    formTaskBinding.statusSwitch.setChecked(dataTodos.isStatusTodos());
                    formTaskBinding.btnSave.setText("Update Task");
                }

                formTaskBinding.btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(formTaskBinding.etTaskName.getText())){
                            formTaskBinding.etTaskName.setError("Data not empty");
                        }else{
                            Client client = new Client();
                            TodosService service = client.Client(TodosService.class, ApiHelper.BASE_URL);
                            DataTodos data = new DataTodos();
                            data.setTaskTodos(formTaskBinding.etTaskName.getText().toString());
                            data.setStatusTodos(formTaskBinding.statusSwitch.isChecked());
                            data.setIdTodos(dataTodos.getIdTodos());

                            service.updateTodos(dataTodos.getIdTodos(), data).enqueue(new Callback<DeleteModels>() {
                                @Override
                                public void onResponse(Call<DeleteModels> call, Response<DeleteModels> response) {
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<DeleteModels> call, Throwable t) {
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onDelete(DataTodos dataTodos) {
                viewModels.deleteTodos(dataTodos.getIdTodos());
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onUpdateStatus(DataTodos dataTodos) {
                DataTodos todosData = new DataTodos();
                if (dataTodos.isStatusTodos()) {
                    todosData.setStatusTodos(false);
                } else {
                    todosData.setStatusTodos(true);
                }
                todosData.setTaskTodos(dataTodos.getTaskTodos());
                todosData.setIdTodos(dataTodos.getIdTodos());

                Client client = new Client();
                TodosService service = client.Client(TodosService.class, ApiHelper.BASE_URL);
                service.updateTodos(dataTodos.getIdTodos(), todosData).enqueue(new Callback<DeleteModels>() {
                    @Override
                    public void onResponse(Call<DeleteModels> call, Response<DeleteModels> response) {
                        String st = new Gson().toJson(response.body());
                        Log.d("Status Update ", st);
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onFailure(Call<DeleteModels>call, Throwable t) {
                        Log.e("Status Eror Update ", t.getMessage());
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                });
            }
        });
    }

    public void getAllDataTodos() {
        viewModels = ViewModelProviders.of(this).get(MainViewModels.class);
        viewModels.getTodos().observe(this, new Observer<TodosResponse>() {
            @Override
            public void onChanged(TodosResponse todosResponse) {
                List<DataTodos> dataTodos = todosResponse.getDataTodos();
                todosAdapter.setTodos(getApplicationContext(), dataTodos);
                binding.rvTodos.setAdapter(todosAdapter);
                binding.rvTodos.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false));
                showLoading(false);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.pbLoading.setVisibility(View.VISIBLE);
        } else {
            binding.pbLoading.setVisibility(View.GONE);
        }
    }

    public void CreateNewData(View view) {
        ShowForm();
    }

    private void ShowForm(){
        FormTaskBinding formTaskBinding;
        formTaskBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.form_task);
        formTaskBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(formTaskBinding.etTaskName.getText())){
                    formTaskBinding.etTaskName.setError("Data not empty");
                }else{
                    Client client = new Client();
                    TodosService service = client.Client(TodosService.class, ApiHelper.BASE_URL);
                    DataTodos data = new DataTodos();
                    data.setTaskTodos(formTaskBinding.etTaskName.getText().toString());

                    service.addTodos(data).enqueue(new Callback<List<TodosResponse>>() {
                        @Override
                        public void onResponse(Call<List<TodosResponse>> call, Response<List<TodosResponse>> response) {
                            String js = new Gson().toJson(response.body());
                            Log.d("Get Post", js);
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<List<TodosResponse>> call, Throwable t) {
                            Log.e("Get Post", t.getMessage());
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }
}