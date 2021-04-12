package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

import io.perfmark.Tag;

public class register extends AppCompatActivity {
    Button go_back_login, start_re;
    TextInputLayout full_name,input_email,input_phone,password_1, confirm_password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String user_ID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        go_back_login = findViewById(R.id.go_back_login);
        start_re = findViewById(R.id.start_register);
        full_name = findViewById(R.id.input_full_name);
        input_email = findViewById(R.id.input_Email);
        input_phone = findViewById(R.id.input_phone);
        password_1 = findViewById(R.id.input_password_1);
        confirm_password = findViewById(R.id.input_password_2);
        progressBar = findViewById(R.id.progessbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
            finish();
        }

        go_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        start_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = full_name.getEditText().getText().toString().trim();
                String email = input_email.getEditText().getText().toString().trim();
                String phone = input_phone.getEditText().getText().toString().trim();

                if (!vaildname() | !vaildEmail() | !vaildphone() | !vaildpassword() | !vaildpassword_2()) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(input_email.getEditText().getText().toString(),  password_1.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "註冊成功!!!", Toast.LENGTH_SHORT).show();
                            user_ID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users").document(user_ID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("phone", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "onSuccess is created for" + user_ID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure" + e.toString());
                                }
                            });
                            startActivity(new Intent().setClass(getApplicationContext(),UserProfile.class));
                            finish();

                        } else {
                            Toast.makeText(register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private Boolean vaildEmail() {
        String email = input_email.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            input_email.setError("Email is Required");
            return false;
        } else {
            input_email.setError(null);
            return true;
        }
    }

    private Boolean vaildname() {
        String name  = full_name.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            full_name.setError("本欄位必須填寫");
            return false;
        } else {
            full_name.setError(null);
            return true;
        }
    }

    private Boolean vaildphone() {
        String phone  = input_phone.getEditText().getText().toString().trim();
        if (phone.isEmpty()) {
            input_phone.setError("本欄位必須填寫");
            return false;
        } else {
            input_phone.setError(null);
            return true;
        }
    }

    private Boolean vaildpassword() {
        String Password_1  = password_1.getEditText().getText().toString().trim();
        if (Password_1.isEmpty()) {
            password_1.setError("本欄位必須填寫");
            return false;
        } else if (Password_1.length() < 6) {
            password_1.setError("密碼必須至少6個數");
            return false;
        } else {
            password_1.setError(null);
            return true;
        }
    }

    private Boolean vaildpassword_2() {
        String Password_1  = password_1.getEditText().getText().toString().trim();
        String Password_2  = confirm_password.getEditText().getText().toString().trim();
        if (Password_2.isEmpty()) {
            confirm_password.setError("本欄位必須填寫");
            return false;
        }else if (Password_2.length() < 6) {
            confirm_password.setError("密碼必須至少6個數");
            return false;
        }
        else if (!Password_1.equals(Password_2)) {
            confirm_password.setError("密碼確認錯誤");
            return false;
        } else {
            confirm_password.setError(null);
            return true;
        }
    }
    public boolean onKeyDown(int KeyCode , KeyEvent event) {
        if (KeyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(KeyCode, event);
    }
}
