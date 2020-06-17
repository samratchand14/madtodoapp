package com.schand.madtodo.ui;

import android.app.Application;

import com.schand.madtodo.database.TodoRepository;
import com.schand.madtodo.models.Todo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class TodoViewModel extends AndroidViewModel {

    //check modelviewviewmodel MVVM android
    private TodoRepository repository;
    private LiveData<List<Todo>> allTodosForUser;
    private int user_id;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        user_id=0;
        repository = new TodoRepository(application);
    }

    public void insert(Todo todo){
        repository.insert(todo);
    }
    public void update(Todo todo){
        repository.update(todo);
    }

    public void delete(Todo todo){

        repository.delete(todo);
    }

    public LiveData<List<Todo>> getAllTodosForUser(int user_id){
        allTodosForUser = repository.getAllTodosForUser(user_id);
        return allTodosForUser;
    }

    public  LiveData<List<Todo>> setAllTodosForUserCategory(int user_id, int category) {
        allTodosForUser = repository.getAllTodosForUserCategory(user_id,category);
        return allTodosForUser;
    }


    public void archive(Todo todo) {
        repository.archive(todo);
    }

    public void archiveAllCompletedTodosForUser(int user_id) {
        repository.archiveAllTodosForUser(user_id);
    }

    public void completeAllTodosForUser(int user_id) {
        repository.completeAllTodosForUser(user_id);
    }

    public void markAllTodosIncompleteForUser(int user_id) {
        repository.markAllTodosIncompleteForUser(user_id);
    }

    public void bringAllFromArchiveForUser(int user_id) {
        repository.bringAllFromArchiveForUser(user_id);
    }

    public void complete(Todo todo) {
        repository.complete(todo);
    }

    public void deleteAllArchivedForUser(int user_id) {
        repository.deleteAllArchivedForUser(user_id);
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public LiveData<List<Todo>> getAllTodosForUserCategory(int user_id, int category) {

        allTodosForUser = repository.getAllTodosForUserCategory(user_id, category);
        return allTodosForUser;
    }


}