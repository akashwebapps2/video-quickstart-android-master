package com.twilio.video.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.twilio.video.test.Model.DeviceListResponse;
import com.twilio.video.test.R;
import com.twilio.video.test.adapter.UserListAdapter;
import com.twilio.video.test.util.UserPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecipientListActivity extends AppCompatActivity {


    RecyclerView rv_userlist;
    String m_androidId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_list);
        m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rv_userlist=findViewById(R.id.rv_userlist);

        getDeviceList();
    }

    public void getDeviceList() {
        String server_url = "https://phpwebdevelopmentservices.com/development/twilo/get-all-user";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", m_androidId);
            // jsonObject.put("roomId", roomName);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("resp", response.toString());

                DeviceListResponse model
                        =new Gson().fromJson(response.toString(),DeviceListResponse.class);
                rv_userlist.setAdapter(new UserListAdapter(RecipientListActivity.this,model.getData()));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        Volley.newRequestQueue(RecipientListActivity.this).add(request);
    }
}