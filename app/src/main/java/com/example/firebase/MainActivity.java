package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView TV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TV = findViewById(R.id.home);
        TV.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    startActivity(new Intent(MainActivity.this, Login.class));
                    MainActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}