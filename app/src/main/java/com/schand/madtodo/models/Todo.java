package com.schand.madtodo.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = CASCADE))
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final int user_id;
    private String title;
    private String description;
    private String duedate;
    private int priority;
    private int category;
    private boolean is_archived;
    private boolean is_completed;

    public static final int CATEGORY_HOME=0;
    public static final int CATEGORY_OFFICE=1;
    public static final int CATEGORY_FRIENDS=2;
    public static final int CATEGORY_FAMILY=3;


    public static final int PRIORITY_HIGH=0;
    public static final int PRIORITY_MEDIUM=1;
    public static final int PRIORITY_LOW=2;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isIs_archived() {
        return is_archived;
    }

    public void setIs_archived(boolean is_archived) {
        this.is_archived = is_archived;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }





    public Todo(String title, String description, int priority, int category, String duedate, int user_id) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.duedate = duedate;
        this.user_id = user_id;
        this.is_archived = false;
        this.is_completed = false;
    }



}

