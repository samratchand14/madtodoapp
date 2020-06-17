package com.schand.madtodo.database;

import com.schand.madtodo.models.Todo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TodoDao {
    @Insert
    void insert(Todo task);

    @Update
    void update(Todo task);

    @Delete
    void delete(Todo task);

    @Query("SELECT * FROM todo WHERE is_archived = 0 and user_id=:user_id ORDER BY id desc")
    LiveData<List<Todo>> getAllTodosForUser(int user_id);

    @Query("SELECT * FROM todo WHERE is_archived = 0 and user_id=:user_id and category=:category ORDER BY id desc")
    LiveData<List<Todo>> getAllTodosForUserCategory(int user_id, int category);

    @Query("UPDATE todo SET is_archived = 1 WHERE id =:id ")
    void archive(int id);

    @Query("UPDATE todo SET is_archived =1 where user_id=:user_id and is_completed=1")
    void archiveAllCompletedTodosForUser(int user_id);

    @Query("UPDATE todo set is_completed=1 WHERE is_archived=0 and user_id=:user_id")
    void completeAllTodos(Integer user_id);

    @Query("UPDATE todo set is_completed=0 WHERE is_archived=0 and user_id=:user_id")
    void markAllTodosIncomplete(Integer user_id);

    @Query("UPDATE todo set is_archived=0 and user_id=:user_id")
    void bringAllFromArchive(Integer user_id);

    @Query(("UPDATE todo set is_completed=1 where id =:id"))
    void complete(int id);

    @Query("DELETE from todo WHERE is_archived=1 and user_id=:user_id")
    void deleteAllArchived(Integer user_id);


}

