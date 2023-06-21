package com.example.safekick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email, login_password;
    private Button login_button, join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        login_email = findViewById( R.id.login_email );
        login_password = findViewById( R.id.login_password );

        join_button = findViewById( R.id.join_button );
        join_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
                startActivity( intent );
            }
        });


        login_button = findViewById( R.id.login_button );
        login_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserEmail = login_email.getText().toString();
                String UserPwd = login_password.getText().toString();

                Log.d("id",UserEmail);
                Log.d("pw",UserPwd);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);

                        try {


                            JSONObject jsonObject = new JSONObject( response );
                            //jsonObject["success"] = true;
                            //즉, jsonObject = {"success":true}


                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {//로그인 성공시
                                Log.d("success","successlogin");
                                String UserEmail = jsonObject.getString( "UserEmail" );
                                String UserPwd = jsonObject.getString( "UserPwd" );
                                String UserName = jsonObject.getString( "UserName" );

                                Toast.makeText( getApplicationContext(), String.format("%s님 환영합니다.", UserName), Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, BorrowActivity.class );

                                intent.putExtra( "UserEmail", UserEmail );
                                intent.putExtra( "UserPwd", UserPwd );
                                intent.putExtra( "UserName", UserName );

                                Log.d("UserEmail",UserEmail);
                                startActivity( intent );

                            }

                            else {//로그인 실패시
                                AlertDialog.Builder FailDialog = new AlertDialog.Builder(LoginActivity.this);
                                FailDialog.setTitle("경고!").setMessage("권한이 없습니다.");
                                AlertDialog alertDialog = FailDialog.create();
                                alertDialog.show();


//                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest( UserEmail, UserPwd, responseListener );
                RequestQueue queue = Volley.newRequestQueue( LoginActivity.this );
                queue.add( loginRequest );

            }
        });
    }
}