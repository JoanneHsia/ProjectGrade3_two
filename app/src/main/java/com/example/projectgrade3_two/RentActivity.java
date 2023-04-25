package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class RentActivity extends AppCompatActivity {

    private Activity context=this;

    Button btn_lend, btn_borr;
    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        Button backPageBtn = (Button)findViewById(R.id.rentback_btn);
        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RentActivity.this  ,HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_lend = findViewById(R.id.btn_lend);
        btn_borr = findViewById(R.id.btn_borr);

        btn_lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator=new IntentIntegrator(RentActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        btn_borr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator=new IntentIntegrator(RentActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });





    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult ScanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);

        if(ScanResult !=null){
            if(ScanResult.getContents() != null) {
                String Scan = ScanResult.getContents();
                if(!Scan.equals("")){
                    item_id = Scan.trim();
                    Intent intent = new Intent(RentActivity.this, DoneActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("item_id", item_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
            Toast.makeText(RentActivity.this, "請重新掃描", Toast.LENGTH_SHORT).show();
        }
    }
}