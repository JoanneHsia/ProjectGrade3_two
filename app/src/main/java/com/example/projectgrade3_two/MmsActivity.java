package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MmsActivity extends AppCompatActivity {

    TextView txtUserID, txtHosID;


    String user_id;

    String urllogin = "https://projectgrade3two.000webhostapp.com/userprofile.php";

    private Activity context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mms);

        Bundle bundle =  getIntent().getExtras();
        user_id = bundle.getString("user_id");
        login(user_id);


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

    private void login(String user_id){

        StringRequest request = new StringRequest(Request.Method.POST, urllogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("userProfile");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String userId = object.getString("user_id").trim();
                                    String hosId = object.getString("user_department").trim();

                                    txtUserID = findViewById(R.id.mms_Userid);
                                    txtHosID = findViewById(R.id.mms_hosid);

                                    txtUserID.setText(userId);
                                    txtHosID.setText(hosId);

                                    Log.d("userId", userId);
                                    Log.d("hosId", hosId);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MmsActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MmsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }





}