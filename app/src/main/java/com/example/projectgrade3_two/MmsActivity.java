package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MmsActivity extends AppCompatActivity {

    private Activity context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mms);


        TextView id_U = (TextView) findViewById(R.id.idU);
        TextView id_D = (TextView) findViewById(R.id.idD);

        Bundle bundle =  getIntent().getExtras();
        String a = bundle.getString("idU");
        String b = bundle.getString("idD");
        Log.d("a", a);
        Log.d("b", b);
        id_U.setText(a);
        id_D.setText(b);



        Button backPageBtn = (Button)findViewById(R.id.mmsback_btn);
        Button btn_mmsScan = (Button)findViewById(R.id.btn_mmsScan);
        Spinner spinnermms = findViewById(R.id.spinner_mms);


        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this
                ,R.array.mms_array,android.R.layout.simple_dropdown_item_1line);
        spinnermms.setAdapter(adapter1);


        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MmsActivity.this  ,HomeActivity.class);
                startActivity(intent);
            }
        });
        btn_mmsScan.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator=new IntentIntegrator(context);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        }));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        TextView mmstextView = (TextView)findViewById(R.id.mmstextView);
        IntentResult ScanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,intent);

        if(ScanResult !=null){
            if(ScanResult.getContents() != null) {
                String Scan = ScanResult.getContents();
                if(!Scan.equals("")){
                    mmstextView.setText(Scan.toString());
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,intent);
            mmstextView.setText("產生錯誤");
        }
    }




}