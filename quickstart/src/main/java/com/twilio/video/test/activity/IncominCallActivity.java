package com.twilio.video.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.twilio.video.test.R;

public class IncominCallActivity extends AppCompatActivity {


    Button btn_reject,btn_accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomin_call);
        getSupportActionBar().hide();

        btn_reject=findViewById(R.id.btn_reject);
        btn_accept=findViewById(R.id.btn_accept);

        btn_reject.setOnClickListener(v->finishAffinity());
        btn_accept.setOnClickListener(v->{

            Intent intent=new Intent(this, VideoActivity.class);
            intent.putExtra("type",getIntent().getStringExtra("type"));
            intent.putExtra("room_id",getIntent().getStringExtra("room_id"));
            startActivity(intent);
            finish();

        });
    }
}