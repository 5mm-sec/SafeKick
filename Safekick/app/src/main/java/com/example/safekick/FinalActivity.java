package com.example.safekick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FinalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_final );

        Intent FinalIntent = getIntent();
        String UserEmail = FinalIntent.getStringExtra("UserEmail");
        Log.d("UserEmail",UserEmail);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    System.out.println("빌리기 권한 삭제 ㅅㄱㅋㅋ");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        DeleteAuthRequest deleteAuthRequest = new DeleteAuthRequest(UserEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FinalActivity.this);
        queue.add(deleteAuthRequest);

    }

    }

