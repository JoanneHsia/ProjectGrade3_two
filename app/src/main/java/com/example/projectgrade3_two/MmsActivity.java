package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

    String urlitemClass = "https://projectgrade3two.000webhostapp.com/itemClass.php";
    TableLayout classList;

    TextView txtUserID, txtHosID;

    Button btn_D, btn_E, btn_N;


    String user_id;

    String urllogin = "https://projectgrade3two.000webhostapp.com/userprofile.php";
    String urlUndoUpdate = "https://projectgrade3two.000webhostapp.com/updateUndo.php";


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
        btn_D = findViewById(R.id.btn_D);
        btn_E = findViewById(R.id.btn_E);
        btn_N = findViewById(R.id.btn_N);


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

        btn_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item_class = "D";
                Intent intent = new Intent(MmsActivity.this, TodoListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("item_class", item_class);
                intent.putExtras(bundle);
                startActivity(intent);
                updateUndoData(item_class);
            }
        });
        btn_E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item_class = "E";
                Intent intent = new Intent(MmsActivity.this, TodoListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("item_class", item_class);
                intent.putExtras(bundle);
                startActivity(intent);
                updateUndoData(item_class);
            }
        });

        btn_N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item_class = "N";
                Intent intent = new Intent(MmsActivity.this, TodoListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("item_class", item_class);
                intent.putExtras(bundle);
                startActivity(intent);
                updateUndoData(item_class);
            }
        });

//        spinnermms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView parent, View view, int position, long id) {
//                String item_class = parent.getItemAtPosition(position).toString().substring(0,1);
//                Intent intent = new Intent(MmsActivity.this, TodoListActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("item_class", item_class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                updateUndoData(item_class);

//                itemClass(item_class);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView parent) {
//            }
//        });



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

    private void updateUndoData(String item_class) {

        String item_todo = "undo";


        StringRequest request = new StringRequest(Request.Method.POST, urlUndoUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(MmsActivity.this, "ok3", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MmsActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

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
                params.put("item_todo", item_todo);
                params.put("item_class", item_class);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MmsActivity.this);
        requestQueue.add(request);

    }

//    private void itemClass(String item_class){
//        classList = findViewById(R.id.classList);
//        classList.setStretchAllColumns(true);
//        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
//        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//
//        StringRequest request = new StringRequest(Request.Method.POST, urlitemClass,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//                            JSONArray jsonArray = jsonObject.getJSONArray("classData");
//
//                            if (success.equals("1")) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject object = jsonArray.getJSONObject(i);
//
//                                    TableRow tr = new TableRow(MmsActivity.this);
//                                    tr.setLayoutParams(row_layout);
//                                    tr.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                                    TextView itemId = new TextView(MmsActivity.this);
//                                    itemId.setText(object.getString("item_id").trim());
//                                    itemId.setLayoutParams(view_layout);
//
//                                    TextView itemName = new TextView(MmsActivity.this);
//                                    itemName.setText(object.getString("item_name").trim());
//                                    itemName.setLayoutParams(view_layout);
//
//                                    TextView itemStatus = new TextView(MmsActivity.this);
//                                    itemStatus.setText(object.getString("item_status").trim());
//                                    itemStatus.setLayoutParams(view_layout);
//
//                                    TextView itemDescribe = new TextView(MmsActivity.this);
//                                    itemDescribe.setText(object.getString("item_describe").trim());
//                                    itemDescribe.setLayoutParams(view_layout);
//
//                                    tr.addView(itemId);
//                                    tr.addView(itemName);
//                                    tr.addView(itemStatus);
//                                    tr.addView(itemDescribe);
//                                    classList.addView(tr);
//
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(MmsActivity.this, "err", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MmsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        }
//        ){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("item_class", item_class);
//
//                return params;
//            }
//        };
//
//        Volley.newRequestQueue(this).add(request);
//
//    }





}