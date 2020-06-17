package com.schand.madtodo.database;

import android.content.Context;
import android.os.AsyncTask;

import com.schand.madtodo.models.Todo;
import com.schand.madtodo.models.User;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Todo.class}, version = 3, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    private static TodoDatabase instance;
    public abstract UserDao getUserDao();
    public abstract TodoDao getTodoDao();

    public static synchronized TodoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TodoDatabase.class, "todo_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private UserDao userDao;
        private TodoDao todoDao;

        private PopulateDbAsyncTask(TodoDatabase db){
            userDao = db.getUserDao();
            todoDao = db.getTodoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("test1@test.com","password"));
            userDao.insert(new User("test2@test.com","password"));
            User user1 = userDao.getUserFromEmail("test1@test.com");
            User user2 = userDao.getUserFromEmail("test2@test.com");

            todoDao.insert(new Todo("Home1","Go home", Todo.PRIORITY_MEDIUM,Todo.CATEGORY_HOME ,new Date().toString(), user1.getId()));
            todoDao.insert(new Todo("Office1","Go home", Todo.PRIORITY_MEDIUM,Todo.CATEGORY_HOME ,new Date().toString(), user1.getId()));
            todoDao.insert(new Todo("Family1","Talk with family", Todo.PRIORITY_HIGH, Todo.CATEGORY_FAMILY,new Date().toString(), user1.getId()));
            todoDao.insert(new Todo("Friends1","Talk with friends", Todo.PRIORITY_LOW, Todo.CATEGORY_FRIENDS,new Date().toString(), user1.getId()));

            todoDao.insert(new Todo("Home2","Go home", Todo.PRIORITY_MEDIUM,Todo.CATEGORY_HOME ,new Date().toString(), user2.getId()));
            todoDao.insert(new Todo("Office2","Go home", Todo.PRIORITY_MEDIUM,Todo.CATEGORY_HOME ,new Date().toString(), user2.getId()));
            todoDao.insert(new Todo("Family2","Talk with family", Todo.PRIORITY_HIGH, Todo.CATEGORY_FAMILY,new Date().toString(), user2.getId()));
            todoDao.insert(new Todo("Friends2","Talk with friends", Todo.PRIORITY_LOW, Todo.CATEGORY_FRIENDS,new Date().toString(), user2.getId()));
            return null;
        }
    }

}
