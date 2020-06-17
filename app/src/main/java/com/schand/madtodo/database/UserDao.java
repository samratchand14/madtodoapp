package com.schand.madtodo.database;

import com.schand.madtodo.models.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User where email= :mail and password= :password")
    User getUser(String mail, String password);

    @Query("SELECT * FROM User where email= :mail")
    User getUserFromEmail(String mail);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
