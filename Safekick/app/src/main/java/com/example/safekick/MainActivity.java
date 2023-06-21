package com.example.safekick;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(mIntent);

        finish();
    }

}