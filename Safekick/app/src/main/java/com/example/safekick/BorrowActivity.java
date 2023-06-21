package com.example.safekick;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BorrowActivity extends AppCompatActivity {
    TextView email_text;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        Intent BorrowIntent = getIntent();
        String UserEmail = BorrowIntent.getStringExtra("UserEmail");


        email_text =findViewById(R.id.email_text);
        email_text.setText(UserEmail+"  님 환영합니다.");



        Button Borrow_btn = findViewById(R.id.borrow_btn);

        Borrow_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                JsonObject input = new JsonObject();
                String helmet2 = "helmet";
                try{
                    input.addProperty("helmet_state",helmet2);
                    System.out.println(input);
                }catch(Exception e){

                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://220.69.209.111:8008")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitAPI r = retrofit.create(RetrofitAPI.class);

                Call<registerDATA> call = r.register(input);
                call.enqueue(new Callback<registerDATA>(){
                    @Override
                    public void onResponse(Call<registerDATA> call, Response<registerDATA> response) {

                        registerDATA r = response.body(); //객체 생성 기억하셈
                        Log.d("helmet",r.get_Test()+"");

                        int test = r.get_Test();

                        if(test == 1){
                            Intent myIntent = new Intent(BorrowActivity.this, RidingActivity.class);
                            myIntent.putExtra("UserEmail",UserEmail);
                            startActivityForResult(myIntent, 101);
                        }
                        else if(test == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(BorrowActivity.this);
                            builder.setTitle("킥보드를 안전하게~").setMessage("헬멧을 착용해주세요");
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }


                    }


                    @Override
                    public void onFailure(Call<registerDATA> call, Throwable t) {
                        Log.d("rt",t.getMessage()+"실패실패");
                    }

                });
            }

        });

    }

}
