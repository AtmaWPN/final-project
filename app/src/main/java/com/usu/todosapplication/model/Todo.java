package com.usu.todosapplication.model;

import androidx.annotation.NonNull;
import androidx.core.content.pm.PermissionInfoCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/*
todo
=======================================================================
id int32          |      task varchar(512)  |         isComplete       |
=======================================================================
1                |   go pick up grandma  |           false             |
2                |   do my homework      |           true              |
 */

@Entity
public class Todo {


    @PrimaryKey
    @NonNull
    public String task;

    @ColumnInfo
    public boolean isComplete;

    @ColumnInfo
    public long completions;

    @ColumnInfo
    public long quantity;

    @ColumnInfo
    public boolean visible;

    @ColumnInfo
    public long createdAt;

    @Override
    public boolean equals(Object o) {
        Todo other = (Todo) o;
        return other.task.equals(this.task);
    }
}
