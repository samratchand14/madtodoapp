package com.schand.madtodo.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.schand.madtodo.R;
import com.schand.madtodo.models.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class AddEditTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_ID =
            "com.schand.madtodo.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.schand.madtodo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.schand.madtodo.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.schand.madtodo.EXTRA_PRIORITY";
    public static final String EXTRA_CATEGORY =
            "com.schand.madtodo.EXTRA_CATEGORY";
    public static final String EXTRA_DUEDATE ="com.schand.madtodo.EXTRA_DUEDATE";
    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private TextInputEditText editDueDate;
    private RadioGroup radioPriority, radioCategory;

    private MaterialRadioButton categoryButton, priorityButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_add_task);


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editDueDate=findViewById(R.id.edit_due_date);
        radioPriority = (RadioGroup)findViewById(R.id.edit_priority);
        radioCategory = (RadioGroup)findViewById(R.id.edit_category);


        if(intent.hasExtra(EXTRA_ID)){

            setTitle("Edit Task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            editDueDate.setText(intent.getStringExtra(EXTRA_DUEDATE));
            checkRadioPriorityForIntent(intent.getIntExtra(EXTRA_PRIORITY,0));
            checkRadioCategoryForIntent(intent.getIntExtra(EXTRA_CATEGORY,0));

        }
        else {
            setTitle("Add Task");
        }

        editDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d = format1.parse(editDueDate.getText().toString());
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(),"date picker");
                } catch (ParseException e) {
                    DialogFragment datePicker = new DatePickerFragment();
                    datePicker.show(getSupportFragmentManager(),"date picker");
                }
            }
        });

    }

    /**Checks buttons in Category radiogroup based on category values
     *
     * @param intCategory integer value of category
     */
    private void checkRadioCategoryForIntent(int intCategory) {

        Log.d("category_id",Integer.toString(intCategory));

        switch (intCategory)
        {
            case 0:
                radioCategory.check(R.id.edit_category_home);
                return;

            case 1:
                radioCategory.check(R.id.edit_category_office);
                return;

            case 2:
                radioCategory.check(R.id.edit_category_friends);
                return;

            case 3:
                radioCategory.check(R.id.edit_category_family);
                return;

            default:
                radioCategory.check(R.id.edit_category_home);
                return;
        }

    }

    /**Checks radio button based on priority values
     *
     * @param intPriority Integer value of priority
     */
    private void checkRadioPriorityForIntent(int intPriority) {
        Log.d("priority_id",Integer.toString(intPriority));
        switch(intPriority){
            case 0:
                radioPriority.check(R.id.edit_priority_high);
                return;

            case 1:
                radioPriority.check(R.id.edit_priority_medium);
                return;

            case 2:
                radioPriority.check(R.id.edit_priority_low);
                return;

            default:
                radioPriority.check(R.id.edit_priority_high);
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_task:
                Log.println(Log.INFO,"edit task menu","clicked");
                saveTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**Saves the current values of items in Add to-do view to database
     *
     */
    private void saveTask() {

        //get parameters for task from view
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int selectedPriorityId = radioPriority.getCheckedRadioButtonId() ;
        int selectedCategoryId = radioCategory.getCheckedRadioButtonId();
        priorityButton= findViewById(selectedPriorityId);
        categoryButton=findViewById(selectedCategoryId);
        Log.d("priority",priorityButton.getText().toString());
        Log.d("category",categoryButton.getText().toString());
        String dueDate = editDueDate.getText().toString();


        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description" , Toast.LENGTH_SHORT).show();
            return;
        }

        //finish the save task and return to main activity
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE,title);
        data.putExtra(EXTRA_DESCRIPTION,description);
        data.putExtra(EXTRA_PRIORITY,getPriorityValue(selectedPriorityId));
        data.putExtra(EXTRA_CATEGORY,getCategoryValue(selectedCategoryId));
        data.putExtra(EXTRA_DUEDATE,dueDate);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK,data);
        finish();
    }

    /**Get fixed integer value of priority from priority id
     *
     * @param selectedPriorityId Priority of selected id
     * @return integer value of selected priority
     */
    private int getPriorityValue(int selectedPriorityId) {
        switch (selectedPriorityId){
            case R.id.edit_priority_high:
                return Todo.PRIORITY_HIGH;
            case R.id.edit_priority_medium:
                return Todo.PRIORITY_MEDIUM;
            case R.id.edit_priority_low:
                return Todo.PRIORITY_LOW;
            default:
                return Todo.PRIORITY_HIGH;
        }
    }

    /**Gets fixed integer value of categorys from checked buttonn id
     *
     * @param selectedCategoryID Id of the selected category radio button
     * @return integer value of category id
     */
    private int getCategoryValue(int selectedCategoryID) {
       switch (selectedCategoryID){
           case R.id.edit_category_home:
               return Todo.CATEGORY_HOME;
           case R.id.edit_category_office:
               return Todo.CATEGORY_OFFICE;
           case R.id.edit_category_friends:
               return Todo.CATEGORY_FRIENDS;
           case R.id.edit_category_family:
               return Todo.CATEGORY_FAMILY;
           default:
               return Todo.CATEGORY_HOME;
       }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        editDueDate.setText(format1.format(c.getTime()));
    }


    /** Fragment for datepicker
     *
     */
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }

    }


}