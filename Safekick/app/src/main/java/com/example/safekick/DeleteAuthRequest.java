package com.example.safekick;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteAuthRequest extends StringRequest {
    final static private String URL = "http://safekick.dothome.co.kr/DeleteAuth.php";
    private Map<String, String> map;

    public DeleteAuthRequest(String UserEmail, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("UserEmail", UserEmail);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
