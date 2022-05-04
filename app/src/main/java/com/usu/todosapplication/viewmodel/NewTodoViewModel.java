package com.usu.todosapplication.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usu.todosapplication.model.Todo;
import com.usu.todosapplication.repository.TodosRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewTodoViewModel extends ViewModel {
    TodosRepository repository;
    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    MutableLiveData<Boolean> saving = new MutableLiveData<>();
    MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    @Inject
    public NewTodoViewModel(TodosRepository repository) {
        this.repository = repository;
        errorMessage.setValue("");
        saving.setValue(false);
        saveSuccess.setValue(false);
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getSaving() {
        return saving;
    }

    public MutableLiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public void saveTodo(String task, int quantity) {
        // check to make sure task is not empty
        errorMessage.setValue("");
        saving.setValue(true);
        new Thread(() -> {
            if (task.isEmpty()) {
                errorMessage.postValue("Task cannot be empty");
            } else {
                this.repository.saveTodo(task, quantity);
                saveSuccess.postValue(true);
            }
            saving.postValue(false);
        }).start();
    }
}
