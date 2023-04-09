package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoneActivity extends AppCompatActivity {

    String item_id;

    EditText txtUpDescribe;

    Button btn_done;

    String urlDoneUpdate = "https://projectgrade3two.000webhostapp.com/checkItem.php";
    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";

    TextView txtItemID, txtItemName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        txtUpDescribe = findViewById(R.id.ed_describe);

        btn_done = findViewById(R.id.btn_done);

        Bundle bundle =  getIntent().getExtras();
        item_id = bundle.getString("item_id");

        itemQrcode(item_id);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDoneData(item_id);
                startActivity(new Intent(DoneActivity.this, TodoListActivity.class));
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

                                    txtItemID = findViewById(R.id.txt_itemID);
                                    txtItemName = findViewById(R.id.txt_itemName);

                                    txtItemID.setText(itemId);
                                    txtItemName.setText(itemName);

                                    Toast.makeText(DoneActivity.this, "ok4", Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(DoneActivity.this, "ok5", Toast.LENGTH_SHORT).show();

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

}