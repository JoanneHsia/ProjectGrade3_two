package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

public class TodoListActivity extends AppCompatActivity {

    String item_class, item_id, user_id;

    String urlitemUndo = "https://projectgrade3two.000webhostapp.com/undoItem.php";
    String urlitemDone = "https://projectgrade3two.000webhostapp.com/doneItem.php";
    TableLayout undoList, doneList;

    Button btn_mmsScan, btn_list;

    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);


        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //全螢幕顯示

                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵

        Bundle bundle =  getIntent().getExtras();
        item_class = bundle.getString("item_class");

        itemUndoClass(item_class);
        itemDoneClass(item_class);

        btn_mmsScan = findViewById(R.id.btn_list);
        btn_list.setVisibility(View.INVISIBLE);

        btn_mmsScan = findViewById(R.id.btn_qrcode);

        btn_mmsScan.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator=new IntentIntegrator(TodoListActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        }));

        user_id = bundle.getString("user_id");
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoListActivity.this, ListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                intent.putExtras(bundle);
                startActivity(intent);
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
                    Intent intent = new Intent(TodoListActivity.this, DoneActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("item_id", item_id);
                    bundle.putString("item_class",item_class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
            Toast.makeText(TodoListActivity.this, "請重新掃描", Toast.LENGTH_SHORT).show();
        }
    }

    private void itemUndoClass(String item_class){
        undoList = findViewById(R.id.undo_list);
        undoList.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        StringRequest request = new StringRequest(Request.Method.POST, urlitemUndo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("classUndoData");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    TableRow trUndo = new TableRow(TodoListActivity.this);
                                    trUndo.setLayoutParams(row_layout);
                                    trUndo.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView itemId = new TextView(TodoListActivity.this);
                                    itemId.setText(object.getString("item_id").trim());
                                    itemId.setLayoutParams(view_layout);

                                    TextView itemName = new TextView(TodoListActivity.this);
                                    itemName.setText(object.getString("item_name").trim());
                                    itemName.setLayoutParams(view_layout);

                                    TextView itemStatus = new TextView(TodoListActivity.this);
                                    itemStatus.setText(object.getString("item_status").trim());
                                    itemStatus.setLayoutParams(view_layout);

                                    TextView itemDescribe = new TextView(TodoListActivity.this);
                                    itemDescribe.setText(object.getString("item_describe").trim());
                                    itemDescribe.setLayoutParams(view_layout);

                                    trUndo.addView(itemId);
                                    trUndo.addView(itemName);
                                    trUndo.addView(itemStatus);
                                    trUndo.addView(itemDescribe);
                                    undoList.addView(trUndo);

                                    if (trUndo.getChildCount() == 0){
                                        btn_list.setVisibility(View.VISIBLE);
                                    }
                                    else {

                                    }
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TodoListActivity.this, "err", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TodoListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_class", item_class);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    private void itemDoneClass(String item_class){
        doneList = findViewById(R.id.done_list);
        doneList.setStretchAllColumns(true);
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        StringRequest request = new StringRequest(Request.Method.POST, urlitemDone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("classDoneData");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    TableRow trDone = new TableRow(TodoListActivity.this);
                                    trDone.setLayoutParams(row_layout);
                                    trDone.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView itemId = new TextView(TodoListActivity.this);
                                    itemId.setText(object.getString("item_id").trim());
                                    itemId.setLayoutParams(view_layout);

                                    TextView itemName = new TextView(TodoListActivity.this);
                                    itemName.setText(object.getString("item_name").trim());
                                    itemName.setLayoutParams(view_layout);

                                    TextView itemStatus = new TextView(TodoListActivity.this);
                                    itemStatus.setText(object.getString("item_status").trim());
                                    itemStatus.setLayoutParams(view_layout);

                                    TextView itemDescribe = new TextView(TodoListActivity.this);
                                    itemDescribe.setText(object.getString("item_describe").trim());
                                    itemDescribe.setLayoutParams(view_layout);

                                    trDone.addView(itemId);
                                    trDone.addView(itemName);
                                    trDone.addView(itemStatus);
                                    trDone.addView(itemDescribe);
                                    doneList.addView(trDone);


                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TodoListActivity.this, "err", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TodoListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_class", item_class);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    private void inserData() {
        String user_id = txtUserID.getText().toString().trim();
        String user_name = txtUserName.getText().toString().trim();
        String hos_department = txtHosID.getText().toString().trim();


        StringRequest request = new StringRequest(Request.Method.POST, urlInsert,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(TodoListActivity.this, "ok", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TodoListActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TodoListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("user_name", user_name);
                params.put("hos_department", hos_department);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);

    }


}