package com.schand.madtodo.database;

import android.app.Application;
import android.os.AsyncTask;

import com.schand.madtodo.models.User;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {

        TodoDatabase database = TodoDatabase.getInstance(application);
        userDao = database.getUserDao();
    }

    public void insert(User user) {

        new InsertUserAsyncTask(userDao).execute(user);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

}
