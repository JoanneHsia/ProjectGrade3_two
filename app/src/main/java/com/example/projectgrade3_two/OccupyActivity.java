package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OccupyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupy);

        Button backPageBtn = (Button)findViewById(R.id.occupyback_btn);
        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(OccupyActivity.this  ,HomeActivity.class);
                startActivity(intent);
            }
        });


    }
}