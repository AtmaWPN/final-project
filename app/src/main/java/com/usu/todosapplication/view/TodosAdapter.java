package com.usu.todosapplication.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.usu.todosapplication.R;
import com.usu.todosapplication.model.Todo;

import java.util.ArrayList;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {

    public interface OnTodoClick {
        public void onClick(Todo todo);
    }

    ObservableArrayList<Todo> todos;
    OnTodoClick eventListener;

    public TodosAdapter(
            ObservableArrayList<Todo> todos,
            OnTodoClick eventListener
    ) {
        this.todos = todos;
        this.eventListener = eventListener;
        todos.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Todo>>() {
            @Override
            public void onChanged(ObservableList<Todo> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Todo> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<Todo> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<Todo> sender, int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Todo> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
        checkBox.setChecked(todo.isComplete);
        checkBox.setText(todo.task);
        checkBox.setOnClickListener(view -> {
            eventListener.onClick(todo);
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
