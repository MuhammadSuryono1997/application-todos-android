package com.yono.mytodos.api.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yono.mytodos.R;
import com.yono.mytodos.api.models.DataTodos;
import com.yono.mytodos.api.response.TodosResponse;
import com.yono.mytodos.databinding.ItemTodosBinding;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.MyViewHolder> implements Filterable {
    Context context;
    private List<DataTodos> dataTodos = new ArrayList<>();
    private List<DataTodos> dataTodosFiltered;

    public void setTodos(Context context, final List<DataTodos> dataTodosList){
        this.context = context;
        this.dataTodos = dataTodosList;
        this.dataTodosFiltered = dataTodosList;

        String js = new Gson().toJson(this.dataTodos);
        Log.d("Get adapter", js);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataTodosFiltered = dataTodos;
                } else {
                    ArrayList<DataTodos> filteredList = new ArrayList<>();
                    for (DataTodos post : dataTodos) {
                        if (post.getTaskTodos().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(post);
                        }
                    }
                    dataTodosFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataTodosFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataTodosFiltered = (ArrayList<DataTodos>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface TodosListener {
        void onUpdate(DataTodos dataTodos);

        void onDelete(DataTodos dataTodos);

        void onUpdateStatus(DataTodos dataTodos);
    }

    private TodosListener listener;

    public void setListener(TodosListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public TodosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTodosBinding itemTodosBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_todos,
                parent,
                false
        );

        Log.d("Get home", "Res"+dataTodos);
        return new MyViewHolder(itemTodosBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull TodosAdapter.MyViewHolder holder, int position) {

        holder.BinData(dataTodosFiltered.get(position));
    }

    @Override
    public int getItemCount() {

        Log.d("Get adapter 2", "size"+dataTodos.size());
        return dataTodosFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTodosBinding itemTodosBinding;
        public MyViewHolder(ItemTodosBinding binding) {
            super(binding.getRoot());
            this.itemTodosBinding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void BinData(DataTodos dataTodos){
            itemTodosBinding.setTodos(dataTodos);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .withZone(ZoneId.of("UTC"));

            LocalDateTime date = LocalDateTime
                    .parse(dataTodos.getUpdatedAtTodos(), dateTimeFormatter);

            DateTimeFormatter localFormatter = DateTimeFormatter
                    .ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));

            String localDate = date.format(localFormatter);


            itemTodosBinding.setImages(dataTodos.isStatusTodos());
            itemTodosBinding.setDate("Last Update "+localDate);
            itemTodosBinding.btnDelete.setOnClickListener(view -> listener.onDelete(dataTodos));
            itemTodosBinding.btnEdit.setOnClickListener(view -> listener.onUpdate(dataTodos));

            itemTodosBinding.imageStatus.setOnClickListener(view -> listener.onUpdateStatus(dataTodos));
        }
    }


}
