package com.example.projectgrade3_two;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class DoneActivity extends AppCompatActivity {

    String item_id, item_class, user_id, item_status;

    EditText txtUpDescribe;

    Button btn_done, btn;

    String urlDoneUpdate = "https://projectgrade3two.000webhostapp.com/checkItem.php";
    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";
    String urlremark = "https://projectgrade3two.000webhostapp.com/remark.php";
    String urljudge = "https://projectgrade3two.000webhostapp.com/judge.php";
    String urltodo = "https://projectgrade3two.000webhostapp.com/todo.php";

    String urlinsertMMSD = "https://projectgrade3two.000webhostapp.com/insertMMSDetail.php";

    TextView txtItemID, txtItemName, txtNoremark, txtNote;
    AlertDialog builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //全螢幕顯示

                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵

        txtUpDescribe = findViewById(R.id.ed_describe);
        txtNoremark = findViewById(R.id.txt_Noremark);

        txtNoremark.setVisibility(View.INVISIBLE);
        txtUpDescribe.setVisibility(View.INVISIBLE);

        btn_done = findViewById(R.id.btn_done);
        btn_done.setVisibility(View.INVISIBLE);

        Bundle bundle =  getIntent().getExtras();
        item_id = bundle.getString("item_id");
        item_class = bundle.getString("item_class");
        user_id = bundle.getString("user_id");


        itemQrcode(item_id);
        remark(item_id);
        item_status = "閒置中";
        judge(item_id,item_status);
        todo(item_id);


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDoneData(item_id);
                inseertMMSData(item_id);
                Intent intent = new Intent(DoneActivity.this, TodoListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("item_class",item_class);
                bundle.putString("user_id",user_id);
                intent.putExtras(bundle);
                startActivity(intent);
//                startActivity(new Intent(DoneActivity.this, TodoListActivity.class));
            }
        });
    }

    private void itemQrcode(String item_id){

        StringRequest request = new StringRequest(Request.Method.POST, urlitemid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("itemProfile");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String itemId = object.getString("item_id").trim();
                                    String itemName = object.getString("item_name").trim();
                                    String itemNote = object.getString("item_note").trim();


                                    txtItemID = findViewById(R.id.txt_itemID);
                                    txtItemName = findViewById(R.id.txt_itemName);
                                    txtNote = findViewById(R.id.txt_note);

                                    txtItemID.setText(itemId);
                                    txtItemName.setText(itemName);
                                    txtNote.setText(itemNote);

//                                    Toast.makeText(DoneActivity.this, "ok4", Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoneActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", item_id);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    private void updateDoneData(String item_id) {

        String item_todo = "done";
        String item_describe = txtUpDescribe.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, urlDoneUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
//                                Toast.makeText(DoneActivity.this, "ok5", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoneActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_todo", item_todo);
                params.put("item_id", item_id);
                params.put("item_describe", item_describe);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DoneActivity.this);
        requestQueue.add(request);

    }
    private void remark(String item_id){
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        StringRequest request = new StringRequest(Request.Method.POST, urlremark,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("remrkData");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    TableRow trUndo = new TableRow(DoneActivity.this);

                                    trUndo.setLayoutParams(row_layout);
                                    trUndo.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView itemId = new TextView(DoneActivity.this);
                                    itemId.setText(object.getString("item_id").trim());
                                    itemId.setLayoutParams(view_layout);

                                    TextView itemName = new TextView(DoneActivity.this);
                                    itemName.setText(object.getString("item_name").trim());
                                    itemName.setLayoutParams(view_layout);

                                    txtNoremark.setVisibility(View.VISIBLE);


                                }

                            } else if (success.equals("2")) {
                                txtUpDescribe.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoneActivity.this, "err", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", item_id);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }
    private void judge(String item_id,String item_status){
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        StringRequest request = new StringRequest(Request.Method.POST, urljudge,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("judgeData");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    TableRow trUndo = new TableRow(DoneActivity.this);

                                    trUndo.setLayoutParams(row_layout);
                                    trUndo.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView itemId = new TextView(DoneActivity.this);
                                    itemId.setText(object.getString("item_id").trim());
                                    itemId.setLayoutParams(view_layout);

                                    TextView itemName = new TextView(DoneActivity.this);
                                    itemName.setText(object.getString("item_name").trim());
                                    itemName.setLayoutParams(view_layout);

                                    btn_done.setVisibility(View.VISIBLE);


                                }

                            } else if (success.equals("2")) {
                                AlertDialog.Builder builder =  new AlertDialog.Builder(DoneActivity.this);
                                builder.setIcon(R.drawable.warning)
                                        .setTitle("不當作業！")
                                        .setMessage("此物品尚未執行歸還作業，請確認此物品當前狀態");

                                builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(DoneActivity.this, TodoListActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("item_id", item_id);
                                        bundle.putString("item_class",item_class);
                                        bundle.putString("user_id",user_id);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoneActivity.this, "err" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", item_id);
                params.put("item_status", item_status);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }
    private void todo(String item_id){
        TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams view_layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        StringRequest request = new StringRequest(Request.Method.POST, urltodo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("todoData");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    TableRow trUndo = new TableRow(DoneActivity.this);

                                    trUndo.setLayoutParams(row_layout);
                                    trUndo.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView itemId = new TextView(DoneActivity.this);
                                    itemId.setText(object.getString("item_id").trim());
                                    itemId.setLayoutParams(view_layout);

                                    TextView itemName = new TextView(DoneActivity.this);
                                    itemName.setText(object.getString("item_name").trim());
                                    itemName.setLayoutParams(view_layout);

                                    btn_done.setVisibility(View.VISIBLE);


                                }

                            } else if (success.equals("2")) {
                                AlertDialog.Builder builder =  new AlertDialog.Builder(DoneActivity.this);
                                builder.setIcon(R.drawable.warning)
                                        .setTitle("誤重複掃描物品！")
                                        .setMessage("此物品已完成點班，請返回點班頁面確認點班項目，並掃描其他物品");

                                // 取消按钮
                                builder.setNegativeButton("確認", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(DoneActivity.this, TodoListActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("item_id", item_id);
                                        bundle.putString("item_class",item_class);
                                        bundle.putString("user_id",user_id);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoneActivity.this, "err", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", item_id);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }
    private void inseertMMSData(String id) {

        String mms_item_id = item_id;
        String mms_userID = user_id;

        StringRequest request = new StringRequest(Request.Method.POST, urlinsertMMSD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
//                                Toast.makeText(DoneActivity.this, "ok5", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoneActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mms_item_id", mms_item_id);
                params.put("mms_userID", mms_userID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DoneActivity.this);
        requestQueue.add(request);

    }



}