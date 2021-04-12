package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button go_login,forget_password, go_register;
    TextInputLayout login_e, password;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        go_login = findViewById(R.id.go_login);
        forget_password = findViewById(R.id.go_forget_password);
        go_register = findViewById(R.id.go_register);
        login_e = findViewById(R.id.login_email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progessbar_login);
        firebaseAuth = FirebaseAuth.getInstance();

        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !vaildEmail()  | !vaildpassword() ) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(login_e.getEditText().getText().toString(), password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "登入成功!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserProfile.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset Link Sent To Your Email.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error ! Reset Link Sent is Not Sent."+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

        go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Login.this,register.class));
                finish();
            }
        });


    }

    private Boolean vaildEmail() {
        String email = login_e.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            login_e.setError("Email is Required");
            return false;
        } else {
            login_e.setError(null);
            return true;
        }
    }

    private Boolean vaildpassword() {
        String Password_1  = password.getEditText().getText().toString().trim();
        if (Password_1.isEmpty()) {
            password.setError("本欄位必須填寫");
            return false;
        }
        else if (Password_1.length() < 6) {
            password.setError("密碼必須至少6個數");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ConfirmExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ConfirmExit() {
        AlertDialog.Builder ad = new AlertDialog.Builder(Login.this);
        ad.setTitle("離開");
        ad.setMessage("是否要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
            finish();
        }
    }

}