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

    String item_class, item_id;

    String urlitemUndo = "https://projectgrade3two.000webhostapp.com/undoItem.php";
    String urlitemDone = "https://projectgrade3two.000webhostapp.com/doneItem.php";
    TableLayout undoList, doneList;

    Button btn_mmsScan;

    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Bundle bundle =  getIntent().getExtras();

        item_class = bundle.getString("item_class");

        itemUndoClass(item_class);
        itemDoneClass(item_class);

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

                                    TableRow tr = new TableRow(TodoListActivity.this);
                                    tr.setLayoutParams(row_layout);
                                    tr.setGravity(Gravity.CENTER_HORIZONTAL);

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

                                    tr.addView(itemId);
                                    tr.addView(itemName);
                                    tr.addView(itemStatus);
                                    tr.addView(itemDescribe);
                                    undoList.addView(tr);

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

                                    TableRow tr = new TableRow(TodoListActivity.this);
                                    tr.setLayoutParams(row_layout);
                                    tr.setGravity(Gravity.CENTER_HORIZONTAL);

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

                                    tr.addView(itemId);
                                    tr.addView(itemName);
                                    tr.addView(itemStatus);
                                    tr.addView(itemDescribe);
                                    undoList.addView(tr);

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


}