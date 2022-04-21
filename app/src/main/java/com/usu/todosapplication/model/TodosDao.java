package com.usu.todosapplication.model;

// CRUD
// Create
// Read
// Update
// Delete

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodosDao {

    @Query("SELECT * FROM todo WHERE visible = 1")
    public List<Todo> getTodos();

    @Query("SELECT * FROM todo WHERE completions > 0")
    public List<Todo> getCompletions();

    @Query("SELECT * FROM todo WHERE lower(task) = lower(:task) LIMIT 1")
    public List<Todo> getMatchingTodos(String task);

//    @Query("SELECT * FROM todo WHERE isComplete = 1")
//    public List<Todo> getCompletedTodos();
//
//    @Query("SELECT isComplete FROM todo WHERE id = :id")
//    public Boolean getIsTodoCompleted(long id);

    @Insert
    public long createTodo(Todo todo);

    @Update
    public void updateTodo(Todo todo);

    @Delete
    public void deleteTodo(Todo todo);
}
