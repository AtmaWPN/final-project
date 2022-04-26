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
    ObservableArrayList<Todo> quickAccess = new ObservableArrayList<>();
    ObservableArrayList<Todo> qty = new ObservableArrayList<>();
    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    Handler handler = new Handler();
    @Inject
    public TodosViewModel(TodosRepository repository) {
        System.out.println("VM CREATED");
        this.repository = repository;
        saveSuccess.setValue(false);
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


    public ObservableArrayList<Todo> getQuickAccess() {
        this.quickAccess.clear();
        this.repository.getQuickAccess(quickAccess -> {
            this.quickAccess.addAll(quickAccess);
        });
        return this.quickAccess;
    }

    public void toggleTodoStatus(Todo todo) {
        todo.isComplete = !todo.isComplete;
        if (todo.isComplete) {
            todo.completions++;
            getQuickAccess();
        }
        this.repository.updateTodo(todo, (t) -> {
            int index = todos.indexOf(t);
            todos.set(index, t);
        }, e -> {
            errorMessage.setValue(e.getMessage());
            handler.post(() -> {
                errorMessage.setValue("");
            });
        });
    }
    public void updateQuantity(Todo todo, Long qty){
        todo.quantity = qty;
        this.repository.updateTodo(todo, (t) -> {
            int index = todos.indexOf(t);
            todos.set(index, t);
        }, e -> {
            errorMessage.setValue(e.getMessage());
            handler.post(() -> {
                errorMessage.setValue("");
            });
        });
    }


    public void saveTodo(String task) {

        errorMessage.setValue("");
//        saving.setValue(true);
        saveSuccess.setValue(false);
        new Thread(() -> {
            if (task.isEmpty()) {
                errorMessage.postValue("Task cannot be empty");
            } else {
                this.repository.saveTodo(task);
                saveSuccess.postValue(true);
            }
//            saving.postValue(false);
            handler.post(this::getTodos);
        }).start();
    }

    public void Delete(Todo todo){
        new Thread(() -> {
            repository.deleteTodo(todo);
            handler.post(this::getTodos);
        }).start();
    }
}
