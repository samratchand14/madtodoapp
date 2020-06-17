package com.schand.madtodo.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.schand.madtodo.R;
import com.schand.madtodo.models.Todo;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TODO_REQUEST =1;
    public static final int EDIT_TODO_REQUEST =2;
    private TodoViewModel todoViewModel;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("madtodo", MODE_PRIVATE);

        user_id = preferences.getInt("user_id",0);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("userid");
            Log.d("user_id", Integer.toString(user_id));
            //The key argument here must match that used in the other activity
        }

        if(user_id<1){
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.setUserId(user_id);

        //set floatingactionbutton value
        FloatingActionButton buttonAddTask = findViewById(R.id.button_add_todo);
        buttonAddTask.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);
                startActivityForResult(intent, ADD_TODO_REQUEST);
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        final TodoListAdapter adapter = new TodoListAdapter(todoViewModel);
        recyclerView.setAdapter(adapter);


        //Display list of different categories based ou user input
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_menu_home:
                        TodoListAdapter myAdapter1 = new TodoListAdapter(todoViewModel);
                        setListenerForAdapter(myAdapter1);
                        recyclerView.setAdapter(myAdapter1);
                        changeViewForUserCategory(myAdapter1, Todo.CATEGORY_HOME);
                        return true;
                    case R.id.bottom_menu_office:
                        TodoListAdapter myAdapter2 = new TodoListAdapter(todoViewModel);
                        setListenerForAdapter(myAdapter2);
                        recyclerView.setAdapter(myAdapter2);
                        changeViewForUserCategory(myAdapter2, Todo.CATEGORY_OFFICE);
                        return true;

                    case R.id.bottom_menu_friends: ;
                        TodoListAdapter myAdapter3 = new TodoListAdapter(todoViewModel);
                        setListenerForAdapter(myAdapter3);
                        recyclerView.setAdapter(myAdapter3);
                        changeViewForUserCategory(myAdapter3, Todo.CATEGORY_FRIENDS);
                        return true;

                    case R.id.bottom_menu_family:
                        TodoListAdapter myAdapter4 = new TodoListAdapter(todoViewModel);
                        setListenerForAdapter(myAdapter4);
                        recyclerView.setAdapter(myAdapter4);
                        changeViewForUserCategory(myAdapter4, Todo.CATEGORY_FAMILY);
                        return true;

                    default:
                        recyclerView.setAdapter(adapter);
                }
                return true;
            }
        });

        todoViewModel.getAllTodosForUser(user_id).observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> tasks) {
                //update recyclerview
                // Toast.makeText(MainActivity.this,"unchanged",Toast.LENGTH_SHORT).show();
                adapter.setTodos(tasks);

            }
        });

        //swipe to complete and archive tasks
        swipeHandler(recyclerView, adapter);

        setListenerForAdapter(adapter);
    }

    private void setListenerForAdapter(TodoListAdapter adapter) {
        adapter.setOnItemClickListener(new TodoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(MainActivity.this, AddEditTodoActivity.class);

                Log.d("task id" , Integer.toString(todo.getId()));

                intent.putExtra(AddEditTodoActivity.EXTRA_ID, todo.getId());
                intent.putExtra(AddEditTodoActivity.EXTRA_TITLE, todo.getTitle());
                intent.putExtra(AddEditTodoActivity.EXTRA_DESCRIPTION, todo.getDescription());
                intent.putExtra(AddEditTodoActivity.EXTRA_PRIORITY, todo.getPriority());
                intent.putExtra(AddEditTodoActivity.EXTRA_CATEGORY, todo.getCategory());
                intent.putExtra(AddEditTodoActivity.EXTRA_DUEDATE,todo.getDuedate());

                Log.d("This intent" , String.valueOf(intent.getExtras()));
                startActivityForResult(intent, EDIT_TODO_REQUEST);
            }
        });
    }

    private void changeViewForUserCategory(final TodoListAdapter adapter, int categoryHome) {
        todoViewModel.getAllTodosForUserCategory(user_id, categoryHome).observe(MainActivity.this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> tasks) {
                adapter.setTodos(tasks);
            }
        });
    }


    /**Handle swipe interaction
     *
     * @param recyclerView
     * @param adapter
     */
    private void swipeHandler(RecyclerView recyclerView, final TodoListAdapter adapter) {
        //swipe right

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                todoViewModel.archive(adapter.getTodoAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task archive", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    if (dX > 0) {
                        /* Set your color for positive displacement */
                        p.setColor(Color.RED);

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);
                    } else {
                        /* Set your color for negative displacement */
                        p.setColor(Color.BLUE);

                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

        }).attachToRecyclerView(recyclerView);


        //swipe left
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                todoViewModel.complete(adapter.getTodoAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    if (dX > 0) {
                        /* Set your color for positive displacement */
                        p.setColor(Color.GREEN);

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);
                    } else {
                        /* Set your color for negative displacement */
                        p.setColor(Color.BLUE);

                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the result is from adding a new todotitem a new todoitem is created
        if(requestCode == ADD_TODO_REQUEST && resultCode==RESULT_OK){
            String title = data.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTodoActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTodoActivity.EXTRA_PRIORITY,0);
            int category = data.getIntExtra(AddEditTodoActivity.EXTRA_CATEGORY,0);
            String dueDate = data.getStringExtra(AddEditTodoActivity.EXTRA_DUEDATE);

            Todo task = new Todo(title,description,priority,category,dueDate, user_id);
            todoViewModel.insert(task);

            Toast.makeText(this,"Todo saved",Toast.LENGTH_SHORT).show();


        }
        //if result is from updateing existing todoitme todoitem is updated
        else if (requestCode == EDIT_TODO_REQUEST && resultCode==RESULT_OK) {

            int id = data.getIntExtra(AddEditTodoActivity.EXTRA_ID, -1);
            Log.d("Intent task id" , data.getExtras().toString());
            if(id == -1){
                Toast.makeText(this,"Todo can't be updated",Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditTodoActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTodoActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTodoActivity.EXTRA_PRIORITY,0);
            int category = data.getIntExtra(AddEditTodoActivity.EXTRA_CATEGORY,0);
            String dueDate = data.getStringExtra(AddEditTodoActivity.EXTRA_DUEDATE);

            Todo todo = new Todo(title,description,priority,category, dueDate, user_id);
            todo.setId(id);
            todoViewModel.update(todo);
            Toast.makeText(this,"Todo updated",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(this,"Todo not saved",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.archive_all_completed_tasks:
                todoViewModel.archiveAllCompletedTodosForUser(user_id);
                Toast.makeText(this, "All tasks archived",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.complete_all_tasks:
                todoViewModel.completeAllTodosForUser(user_id);
                Toast.makeText(this, "All tasks completed",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.mark_all_incomplete:
                todoViewModel.markAllTodosIncompleteForUser(user_id);
                Toast.makeText(this, "All tasks marked incomplete",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.bring_all_from_archive:
                todoViewModel.bringAllFromArchiveForUser(user_id);
                Toast.makeText(this, "Brought all back from archive",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.delete_all_archived:
                todoViewModel.deleteAllArchivedForUser(user_id);
                Toast.makeText(this,"Deleted all from archive",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.logout_menu:
                SharedPreferences preferences = getSharedPreferences("madtodo",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("user_id",-1);
                editor.apply();
                Intent i = new Intent(MainActivity.this, SplashScreen.class);
                startActivity(i);
                finish();

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
