package com.usu.todosapplication.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.usu.todosapplication.R;
import com.usu.todosapplication.model.Todo;

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodoViewHolder> {

    public interface OnTodoClick {
        public void onClick(Todo todo);
    }

    ObservableArrayList<Todo> todos;
    OnTodoClick eventListener;
    OnTodoClick deleteListener;
    public TodosAdapter(
            ObservableArrayList<Todo> todos,
            OnTodoClick eventListener,
            OnTodoClick deleteListener
    ) {
        this.todos = todos;
        this.eventListener = eventListener;
        this.deleteListener = deleteListener;
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
        checkBox.setOnClickListener(view -> eventListener.onClick(todo));

        EditText quantityBox = holder.itemView.findViewById(R.id.quantity);
        quantityBox.setText(Long.toString(todo.quantity));

        FloatingActionButton deleteButton = holder.itemView.findViewById(R.id.deleteTodo);
        deleteButton.setOnClickListener(view -> deleteListener.onClick(todo));
        // TODO: add a textChangedListener to quantity
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
