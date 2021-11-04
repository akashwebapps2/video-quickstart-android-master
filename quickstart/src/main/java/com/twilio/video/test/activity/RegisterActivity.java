package com.twilio.video.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.twilio.video.test.R;
import com.twilio.video.test.util.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_name;
    Button btn_register;
    String fcm_token;
    String m_androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (UserPref.isLogged()){
            startActivity(new Intent(this,RecipientListActivity.class));
            finish();

        }


        m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        edt_name = findViewById(R.id.edt_name);
        btn_register = findViewById(R.id.btn_register);
        getFirebaseToken();
    }

    public void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("TAG", "Fetching FCM registration token failed", task.getException());
                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                fcm_token = token;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("token", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("regId", token);
                editor.apply();

                // Log and toast
                Log.e("firebase_token", token);
                Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                //Toast.makeText(WelcomeActivity.this, token, Toast.LENGTH_SHORT).show();
                btn_register.setOnClickListener(v->{
                    registerUser(edt_name.getText().toString());
                });


            }
        });
    }


    public void registerUser(String name) {
        String server_url = "https://phpwebdevelopmentservices.com/development/twilo/create-user";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("device_id", m_androidId);
            jsonObject.put("firebase_token", fcm_token);
            // jsonObject.put("roomId", roomName);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("resp", response.toString());
                try {

                    Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();
                    UserPref.setLogged(true);
                    UserPref.setUserName(response.getJSONObject("data").getString("name"));
                    startActivity(new Intent(RegisterActivity.this,RecipientListActivity.class));


                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        Volley.newRequestQueue(RegisterActivity.this).add(request);
    }

}