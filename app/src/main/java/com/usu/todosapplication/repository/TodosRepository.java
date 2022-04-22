package com.usu.todosapplication.repository;

import android.content.Context;
import android.os.Handler;



import androidx.room.Room;


import com.usu.todosapplication.model.AppDatabase;
import com.usu.todosapplication.model.Todo;


import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class TodosRepository {

    AppDatabase db;

    ArrayList<Todo> todos;

    private Handler handler = new Handler();


    public class TodosRespositoryException extends RuntimeException {
        public TodosRespositoryException(String message) {
            super(message);
        }
    }

    public interface TodosCallback {
        public void call(ArrayList<Todo> todos);
    }

    public interface TodoCallback {
        public void call(Todo todo);
    }

    public interface ExceptionCallback {
        public void call(TodosRespositoryException exception);
    }

    @Inject
    public TodosRepository(@ApplicationContext Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "todos-database" ).build();
    }

    public void saveTodo(String task) {
        // save it
        Todo newTodo = new Todo();
        newTodo.task = task;
        newTodo.isComplete = false;
        // TODO save to database
        newTodo.id = db.getTodosDao().createTodo(newTodo);
        todos.add(newTodo);
    }
    public void deleteTodo(Todo todo){
        if (todo != null) {
            new Thread(
                    todos.isComplete = false
                    todos.visible = false
                    todos.quantity = 0
            );
            db.getTodosDao().deleteTodo(todo);
        }
    }

    public void getTodos(TodosCallback callback) {
        if (todos == null) {
            new Thread(() -> {
                todos = (ArrayList<Todo>) db.getTodosDao().getTodos();
                handler.post(() -> {
                   callback.call(todos);
                });
            }).start();
        } else {
            callback.call(todos);
        }
    }

    public void updateTodo(Todo todo, TodoCallback callback, ExceptionCallback eCallback) {
        new Thread(() -> {
            try {
//                throw new TodosRespositoryException("Could not connect to database");
                db.getTodosDao().updateTodo(todo);
                handler.post(() -> {
                    callback.call(todo);
                });
            } catch (TodosRespositoryException e) {
                handler.post(() -> {
                    eCallback.call(e);
                });
            }
        }).start();
    }

}
