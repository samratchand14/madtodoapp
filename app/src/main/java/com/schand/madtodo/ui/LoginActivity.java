package com.schand.madtodo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.schand.madtodo.R;
import com.schand.madtodo.database.TodoDatabase;
import com.schand.madtodo.database.UserDao;
import com.schand.madtodo.models.User;
import com.skydoves.elasticviews.ElasticButton;

public class LoginActivity extends AppCompatActivity {

    private ElasticButton btSignIn;
    private ElasticButton btSignUp;
    private EditText edtEmail;
    private EditText edtPassword;
    private TodoDatabase database;

    private UserDao userDao;
    private ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Check User...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);


        database=TodoDatabase.getInstance(this);

        userDao = database.getUserDao();


        btSignIn = findViewById(R.id.btSignIn);
        btSignUp = findViewById(R.id.btSignUp);

        edtEmail = findViewById(R.id.emailinput);
        edtPassword = findViewById(R.id.passwordinput);



        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emptyValidation()) {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            User user = userDao.getUser(edtEmail.getText().toString(), edtPassword.getText().toString());
                            if(user!=null){

                                SharedPreferences preferences = getSharedPreferences("madtodo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("user_id", user.getId());
                                editor.apply();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("userid", user.getId());
                                Toast.makeText(LoginActivity.this,"Welcome "+user.getEmail(),Toast.LENGTH_SHORT).show();
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Unregistered user, or incorrect", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    }, 1000);

                }else{
                    Toast.makeText(LoginActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean emptyValidation() {
        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }


}
