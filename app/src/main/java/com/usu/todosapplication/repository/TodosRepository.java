package com.usu.todosapplication.repository;

import android.content.Context;
import android.os.Handler;

import androidx.databinding.ObservableArrayList;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.usu.todosapplication.model.AppDatabase;
import com.usu.todosapplication.model.Todo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class TodosRepository {

    AppDatabase db;

    ArrayList<Todo> todos;

    private Handler handler = new Handler();

    public void deleteTodo(Todo todo) {
        todo.isComplete = false;
        todo.visible = false;
        todo.quantity = 0;
    }

    public class TodosRepositoryException extends RuntimeException {
        public TodosRepositoryException(String message) {
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
        public void call(TodosRepositoryException exception);
    }

    @Inject
    public TodosRepository(@ApplicationContext Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "todos-database" ).build();
    }

    public void saveTodo(String task) {
        List<Todo> matchingTodos = db.getTodosDao().getMatchingTodos(task);
        // if task already exists
        if (matchingTodos.size() > 0) {
            matchingTodos.get(0).quantity++;
        } else {
            // save it
            Todo newTodo = new Todo();
            newTodo.task = task;
            newTodo.isComplete = false;
            db.getTodosDao().createTodo(newTodo);
            todos.add(newTodo);
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
        };
    }

    public void updateTodo(Todo todo, TodoCallback callback, ExceptionCallback eCallback) {
        new Thread(() -> {
            try {
//                throw new TodosRepositoryException("Could not connect to database");
                db.getTodosDao().updateTodo(todo);
                handler.post(() -> {
                    callback.call(todo);
                });
            } catch (TodosRepositoryException e) {
                handler.post(() -> {
                    eCallback.call(e);
                });
            }
        }).start();
    }

}
