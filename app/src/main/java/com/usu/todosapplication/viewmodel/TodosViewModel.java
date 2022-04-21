package com.usu.todosapplication.viewmodel;

import android.os.Handler;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usu.todosapplication.model.Todo;
import com.usu.todosapplication.repository.TodosRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TodosViewModel extends ViewModel {
    TodosRepository repository;
    ObservableArrayList<Todo> todos = new ObservableArrayList<>();
    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    Handler handler = new Handler();
    @Inject
    public TodosViewModel(TodosRepository repository) {
        System.out.println("VM CREATED");
        this.repository = repository;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public ObservableArrayList<Todo> getTodos() {
        this.todos.clear();
        this.repository.getTodos(todos -> {
            this.todos.addAll(todos);
        });
        return this.todos;
    }

    public void toggleTodoStatus(Todo todo) {
        todo.isComplete = !todo.isComplete;
        this.repository.updateTodo(todo, (t) -> {
            int index = todos.indexOf(t);
            todos.set(index, t);
        }, e -> {
            errorMessage.setValue(e.getMessage());
            handler.postDelayed(() -> {
                errorMessage.setValue("");
            }, 3000);
        });
    }
//    public void Delete(Todo todo){
//        repository.deleteTodo();
//    }

}
