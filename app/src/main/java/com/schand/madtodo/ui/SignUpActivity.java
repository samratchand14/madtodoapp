package com.schand.madtodo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.schand.madtodo.R;
import com.schand.madtodo.database.TodoDatabase;
import com.schand.madtodo.database.UserDao;
import com.schand.madtodo.models.User;
import com.skydoves.elasticviews.ElasticButton;

public class SignUpActivity extends AppCompatActivity {


    private EditText edtEmail;
    private EditText edtPassword, confirmPassword;

    private ElasticButton btCancel;
    private ElasticButton btRegister;
    String password;

    private UserDao userDao;

    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        edtEmail = findViewById(R.id.emailinput);
        edtPassword = findViewById(R.id.passwordinput);
        confirmPassword = findViewById(R.id.passwordconfirminput);


        btCancel = findViewById(R.id.btCancel);
        btRegister = findViewById(R.id.btRegister);

        userDao = TodoDatabase.getInstance(this).getUserDao();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty()) {

                    User existingUser = userDao.getUserFromEmail(edtEmail.getText().toString());

                    if (existingUser != null) {
                        Toast.makeText(SignUpActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    password = edtPassword.getText().toString();
                    String cpassword = confirmPassword.getText().toString();

                    if (!password.equals(cpassword)) {
                        Toast.makeText(SignUpActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 8) {
                        Toast.makeText(SignUpActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            User user = new User(edtEmail.getText().toString(), password);
                            userDao.insert(user);
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        }
                    }, 1000);

                } else {
                    Toast.makeText(SignUpActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**Check if the Text fields are empty
     *
     * @return boolean vaue
     */
    private boolean isEmpty() {
        if (TextUtils.isEmpty(edtEmail.getText().toString()) || TextUtils.isEmpty(edtPassword.getText().toString()) || TextUtils.isEmpty(confirmPassword.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

}

