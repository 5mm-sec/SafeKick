package com.example.safekick;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://safekick.dothome.co.kr/Register.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public RegisterRequest(String UserEmail, String UserPwd, String UserName,String UserAuth,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("UserEmail", UserEmail);
        map.put("UserPwd", UserPwd);
        map.put("UserName", UserName);
        map.put("UserAuth", UserAuth);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}