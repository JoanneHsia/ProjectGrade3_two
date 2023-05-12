package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RepaireActivity extends AppCompatActivity {

    String item_id, user_id;

    Button btn_repSend;

    String urlRepUpdate = "https://projectgrade3two.000webhostapp.com/updateREP.php";
    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";
    String urlinsertRep = "https://projectgrade3two.000webhostapp.com/insertREP.php";

    TextView txtItemID, txtItemName;

    private Activity context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repaire);

        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //全螢幕顯示

                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵

        Button backPageBtn = (Button)findViewById(R.id.repair_btn);
        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RepaireActivity.this ,HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_repSend = findViewById(R.id.btn_repaire_send);

        Bundle bundle =  getIntent().getExtras();
        item_id = bundle.getString("item_id");
        user_id = bundle.getString(("user_id"));

        itemQrcode(item_id);

        btn_repSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRep(item_id);
                inseertRepData(item_id);
                Intent intent = new Intent(RepaireActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id",user_id);
                intent.putExtras(bundle);
                startActivity(intent);
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

                                    txtItemID = findViewById(R.id.txt_reitem_ID);
                                    txtItemName = findViewById(R.id.txt_repitem_name);

                                    txtItemID.setText(itemId);
                                    txtItemName.setText(itemName);

//                                    Toast.makeText(DoneActivity.this, "ok4", Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RepaireActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RepaireActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
    private void updateRep(String item_id) {

        String item_status = "報修中";

        StringRequest request = new StringRequest(Request.Method.POST, urlRepUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(RepaireActivity.this, "登記成功", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RepaireActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RepaireActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_status", item_status);
                params.put("item_id", item_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RepaireActivity.this);
        requestQueue.add(request);

    }
    private void inseertRepData(String id) {

        String repair_item = item_id;
        String repair_user = user_id;

        StringRequest request = new StringRequest(Request.Method.POST, urlinsertRep,
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
                            Toast.makeText(RepaireActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RepaireActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("repair_item", repair_item);
                params.put("repair_user", repair_user);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RepaireActivity.this);
        requestQueue.add(request);

    }
}