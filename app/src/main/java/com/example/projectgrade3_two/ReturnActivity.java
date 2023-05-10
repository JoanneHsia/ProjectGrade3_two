package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ReturnActivity extends AppCompatActivity {

    String item_id, user_id;

    Button btn_retSend;
    String urlRetUpdate = "https://projectgrade3two.000webhostapp.com/return.php";
    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";

    TextView txtItemID, txtItemName, txtItemStatus;

    private Activity context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        Button backPageBtn = (Button)findViewById(R.id.return_btn);
        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ReturnActivity.this  ,HomeActivity.class);
                startActivity(intent);
            }
        });


        btn_retSend = findViewById(R.id.btn_return_send);

        Bundle bundle =  getIntent().getExtras();
        item_id = bundle.getString("item_id");
        user_id = bundle.getString(("user_id"));

        itemQrcode(item_id);

        btn_retSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRet(item_id);
                Intent intent = new Intent(ReturnActivity.this, HomeActivity.class);
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
                                    String itemStatus = object.getString("item_status").trim();

                                    txtItemID = findViewById(R.id.txt_retItemID);
                                    txtItemName = findViewById(R.id.txt_retItemName);
                                    txtItemStatus = findViewById(R.id.txt_retItemStatus);

                                    txtItemID.setText(itemId);
                                    txtItemName.setText(itemName);
                                    txtItemStatus.setText(itemStatus);

//                                    Toast.makeText(DoneActivity.this, "ok4", Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReturnActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReturnActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
    private void updateRet(String item_id) {

        String item_status = "閒置中";
        String item_occupybed = "0";
        String item_describe = "";
        String item_rentD = "";

        StringRequest request = new StringRequest(Request.Method.POST, urlRetUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(ReturnActivity.this, "歸還成功", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReturnActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReturnActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_status", item_status);
                params.put("item_id", item_id);
                params.put("item_occupybed", item_occupybed);
                params.put("item_describe", item_describe);
                params.put("item_rentD", item_rentD);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ReturnActivity.this);
        requestQueue.add(request);

    }
}