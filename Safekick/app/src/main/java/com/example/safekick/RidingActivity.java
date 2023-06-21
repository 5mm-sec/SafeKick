package com.example.safekick;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RidingActivity extends AppCompatActivity {
    int stack = 0;
    private static final int DIALOG_ID = 1;
    RidingThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);



        Intent RidingIntent = getIntent();
        String UserEmail = RidingIntent.getStringExtra("UserEmail");
        Log.d("UserEmail", UserEmail);

        mThread = new RidingThread(UserEmail);
        mThread.start();

        Button Return_btn = findViewById(R.id.Return_btn);
        Button Accident_btn = findViewById(R.id.Accident_btn);


        Return_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RidingActivity.this);

                Intent myIntent = new Intent(RidingActivity.this, BorrowActivity.class);
                startActivityForResult(myIntent, 101);

                builder.setTitle("세이프kick").setMessage("이용해주셔서 감사합니다.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Accident_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog builder = new AlertDialog.Builder(RidingActivity.this)
                        .setTitle("사고 여부 조사")
                        .setMessage("사고가 발생하셨나요?")
                        .setPositiveButton("네", null)
                        .setNegativeButton("아니요", null)
                        .create();
                //alertDialog.show();
                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveButton = ((AlertDialog) builder).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = ((AlertDialog) builder).getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Log.d("사고발생버튼", "on");
                                //사고나면 처리할거
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("plain/text");
                                String[] address = {"kokl003@naver.com"};
                                email.putExtra(Intent.EXTRA_EMAIL, address);
                                email.putExtra(Intent.EXTRA_SUBJECT, "사고가 발생했습니다.");
                                email.putExtra(Intent.EXTRA_TEXT, "사고가 발생했습니다.");
                                startActivity(email);
                            }
                        });
                        negativeButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                builder.dismiss();
                            }
                        });

                    }
                });
                builder.show();


            }
        });

        }

        class RidingThread extends Thread{
        private  String email = null;
        private  boolean stopped = false;
            public RidingThread(String email){
                this.email = email;
                stopped = false;
            }
            public void run() {
                Log.d("RUN","CALL RUN");
                try {
                    while (!stopped)  {
                        JsonObject input = new JsonObject();
                        String Sentence = "test";
                        try
                        {
                            input.addProperty("EXAMPLE", Sentence);
                            System.out.println(input);
                        } catch(Exception e) {
                        }


                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://220.69.209.111:8008")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        RetrofitAPI r = retrofit.create(RetrofitAPI.class);

                        Log.d("넣은거",input.toString());

                        Call<registerDATA> call = r.register(input);
                        call.enqueue(new Callback<registerDATA>()

                        {

                            @Override
                            public void onResponse
                                    (Call < registerDATA > call, Response< registerDATA > response) {

                                registerDATA r = response.body();

                                Log.d("helmet_state", r.getHelmet_state() + "");
                                Log.d("two_riding", r.getTwo_riding() + "");
                                Log.d("accident", r.getAccidnet() + "");
                               // Log.d("delete_auth",r.getDelete_auth()+"");
                                Log.d("wholeflags", r.getWholeflags() + "");


                                int helmet_on = r.getHelmet_state();
                                int two_riding_on = r.getTwo_riding();
                                int accident = r.getAccidnet();
                                //int delete_auth = r.getDelete_auth();


                                if (stack == 3) { //권한 박탈 날라오면

                                    RidingThread.this.end();
                                    AlertDialog.Builder Delete_Auth_dialog = new AlertDialog.Builder(RidingActivity.this);
                                    Delete_Auth_dialog.setTitle("경고 !").setMessage("킥보드를 빌릴 수 있는 권한이 박탈되었습니다.");
                                    AlertDialog alertDialog3 = Delete_Auth_dialog.create();
                                    alertDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            Log.d("DeleteAuthDISSMISS","CALL");
                                            com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
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


                                            DeleteAuthRequest deleteAuthRequest = new DeleteAuthRequest(email, responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(RidingActivity.this);
                                            queue.add(deleteAuthRequest);

                                            //mThread = RidingThread.this.reStart();

                                            Intent StartIntent = new Intent(RidingActivity.this, LoginActivity.class);
                                            startActivity(StartIntent);

                                        }
                                    });
                                    alertDialog3.show();
                                    Log.d("stack분기로 넘어옴","스택 분기로 넘어옴");

                                }
                                else if (helmet_on == 0) {
                                    RidingThread.this.end();
                                    AlertDialog.Builder no_helmet_dialog = new AlertDialog.Builder(RidingActivity.this);
                                    no_helmet_dialog.setTitle("경고 !").setMessage("헬멧을 착용한 상태로 주행하세요");
                                    AlertDialog alertDialog2 = no_helmet_dialog.create();
                                    alertDialog2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            Log.d("DISSMISS","CALL");
                                            mThread = RidingThread.this.reStart();
                                        }
                                    });
                                    alertDialog2.show();
                                    // 헬멧 착용하세요 다이얼로그 띄우기
                                    stack += 1;
                                    System.out.println("경고 스택 수 : "+stack);
                                } else if (two_riding_on == 1) {
                                    RidingThread.this.end();

                                    //new InterruptedException("야이 시발럼아2");
                                    AlertDialog.Builder two_riding_on_dialog = new AlertDialog.Builder(RidingActivity.this);
                                    two_riding_on_dialog.setTitle("경고 !").setMessage("킥보드엔 한명만 탑승하세요");
                                    AlertDialog alertDialog3 = two_riding_on_dialog.create();
                                    alertDialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            Log.d("DISSMISS","CALL");
                                        }
                                    });
                                    alertDialog3.show();

                                    //킥보드는 혼자서 탑승하세요 다이얼로그 띄우기
                                    System.out.println(stack + "경고 스택 수 : ");
                                    stack += 1;
                                } else if (accident == 1) {
                                    RidingThread.this.end();
                                    AlertDialog accident_dialog = new AlertDialog.Builder(RidingActivity.this)
                                            .setTitle("사고 여부 조사")
                                            .setMessage("사고가 발생하셨나요?")

                                            .setPositiveButton("네", null)
                                            .setNegativeButton("아니요", null)
                                            .create();

                                    accident_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                        @Override
                                        public void onShow(DialogInterface dialogInterface) {
                                            Button positiveButton = ((AlertDialog) accident_dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                                            Button negativeButton = ((AlertDialog) accident_dialog).getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

                                            positiveButton.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    Log.d("사고발생버튼", "on");
                                                    //사고나면 처리할거
                                                    Intent email = new Intent(Intent.ACTION_SEND);
                                                    email.setType("plain/text");
                                                    String[] address = {"kokl003@naver.com"};
                                                    email.putExtra(Intent.EXTRA_EMAIL, address);
                                                    email.putExtra(Intent.EXTRA_SUBJECT, "사고가 발생했습니다.");
                                                    email.putExtra(Intent.EXTRA_TEXT, "사고가 발생했습니다.");
                                                    startActivity(email);
                                                }
                                            });
                                            negativeButton.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    accident_dialog.dismiss();
                                                }
                                            });
                                        }
                                    });
                                    accident_dialog.show();
                                    //사고 나셨나요 yes or no 다이얼로그 띄우기
                                 }
                            }
                            @Override
                            public void onFailure (Call < registerDATA > call, Throwable t){
                                Log.d("rt", t.getMessage() + "실패실패");
                            }
                        });
                        Thread.sleep(1000);
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            public void end(){
                this.stopped = true;
            }

            public RidingThread reStart(){
                RidingThread t = new RidingThread(this.email);
                t.start();
                return  t;

            }

        }


    }

