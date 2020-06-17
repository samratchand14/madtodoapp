package com.schand.madtodo.database;

import android.app.Application;
import android.os.AsyncTask;

import com.schand.madtodo.models.Todo;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodosForUser;

    public TodoRepository(Application application) {

        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao = database.getTodoDao();
    }

    public void insert(Todo todo) {

        new InsertTodoAsyncTask(todoDao).execute(todo);
    }

    public void update(Todo todo) {

        new UpdateTodoAsyncTask(todoDao).execute(todo);
    }

    public void delete(Todo todo) {

        new DeleteTodoAsyncTask(todoDao).execute(todo);

    }

    public LiveData<List<Todo>> getAllTodosForUser(int user_id) {
        allTodosForUser = todoDao.getAllTodosForUser(user_id);
        return allTodosForUser;
    }

    public  LiveData<List<Todo>> getAllTodosForUserCategory(int user_id, int category) {
        allTodosForUser = todoDao.getAllTodosForUserCategory(user_id,category);
        return allTodosForUser;

    }

    public void archive(Todo todo) {
        new ArchiveTodoAsyncTask(todoDao).execute(todo);
    }

    public void archiveAllTodosForUser(int user_id) {
        new ArchiveAllTodosForUserAsyncTask(todoDao).execute(user_id);
    }

    public void completeAllTodosForUser(int user_id) {
        new CompleteAllTasksForUserAsyncTask(todoDao).execute(user_id);
    }

    public void markAllTodosIncompleteForUser(int user_id) {
        new MarkAllTasksIncompleteForUserAsyncTask(todoDao).execute(user_id);
    }

    public void bringAllFromArchiveForUser(int user_id) {
        new BringAllFromArchiveForUserAsyncTask(todoDao).execute(user_id);
    }

    public void complete(Todo todo) {
        new CompleteTodoAsyncTask(todoDao).execute(todo);
    }

    public void deleteAllArchivedForUser(int user_id) {
        new DeleteAllArchivedForUserAsyncTask(todoDao).execute(user_id);
    }



    private static class MyTaskParams {
        int user_id;
        int category;

        MyTaskParams(int user_id, int category) {
            this.user_id = user_id;
            this.category = category;
        }
    }


    private static class CompleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private CompleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }


        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.complete(todos[0].getId());
            return null;
        }
    }

    private static class InsertTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private InsertTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }


        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.insert(todos[0]);
            return null;
        }
    }

    private static class ArchiveTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private ArchiveTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }


        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.archive(todos[0].getId());
            return null;
        }
    }

    private static class UpdateTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private UpdateTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }


        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.update(todos[0]);
            return null;
        }
    }

    private static class DeleteTodoAsyncTask extends AsyncTask<Todo, Void, Void> {

        private TodoDao todoDao;

        private DeleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }


        @Override
        protected Void doInBackground(Todo... todos) {
            todoDao.delete(todos[0]);
            return null;
        }
    }

    private static class ArchiveAllTodosForUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TodoDao todoDao;

        private ArchiveAllTodosForUserAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            todoDao.archiveAllCompletedTodosForUser(ints[0]);
            return null;
        }
    }

    private static class CompleteAllTasksForUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TodoDao todoDao;

        private CompleteAllTasksForUserAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            todoDao.completeAllTodos(ints[0]);
            return null;
        }
    }

    private static class MarkAllTasksIncompleteForUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TodoDao todoDao;

        private MarkAllTasksIncompleteForUserAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            todoDao.markAllTodosIncomplete(ints[0]);
            return null;
        }
    }

    private static class BringAllFromArchiveForUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TodoDao todoDao;

        private BringAllFromArchiveForUserAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            todoDao.bringAllFromArchive(ints[0]);
            return null;
        }
    }

    private static class DeleteAllArchivedForUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TodoDao todoDao;

        private DeleteAllArchivedForUserAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            todoDao.deleteAllArchived(ints[0]);
            return null;
        }
    }

}
