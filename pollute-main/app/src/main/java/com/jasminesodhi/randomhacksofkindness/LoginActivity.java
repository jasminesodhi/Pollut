package com.jasminesodhi.randomhacksofkindness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by jasminesodhi on 16/07/17.
 */

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    FancyButton login;

    static String url, value;
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        url = "http://twentyeight10.tech/rhok.php/user";
        value = getIntent().getExtras().getString("Type");

        editTextEmail = (EditText) findViewById(R.id.name);
        editTextPassword = (EditText) findViewById(R.id.password);

        login = (FancyButton) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAuthorization();
            }
        });
    }

    void userAuthorization() {

        final String userType = value;
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Result response 1", response);

                        showJSON(response);
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Result response 2", error.toString());
                        //Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("type", userType);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json) {

        try {

            JSONObject jsonObject = new JSONObject(json);
            token = jsonObject.getString("token");

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("Token", token);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
