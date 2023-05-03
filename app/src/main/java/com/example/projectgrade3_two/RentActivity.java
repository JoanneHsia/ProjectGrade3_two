package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class RentActivity extends AppCompatActivity {

    private Activity context=this;

    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";
    String urlrent = "https://projectgrade3two.000webhostapp.com/updaterent.php";


    Button btn_rent_send;
    String item_id, user_id, type;

    TextView txtItemID, txtItemName;

    EditText txtDate,txtUser, txtDep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        txtUser = findViewById(R.id.ed_user);
        txtDep = findViewById(R.id.ed_department);
        txtDate = findViewById(R.id.ed_date);

        Bundle bundle =  getIntent().getExtras();
        item_id = bundle.getString("item_id");
        itemQrcode(item_id);

        user_id = bundle.getString("user_id");

        Button backPageBtn = (Button)findViewById(R.id.rentback_btn);
        btn_rent_send = findViewById(R.id.btn_rent_send);
        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_lend:
                        type = "lend";
                        break;
                    case R.id.radio_borrow:
                        type = "borrow";
                        break;
                }
            }
        });

        btn_rent_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRentData();
                Intent intent = new Intent(RentActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
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

                                    txtItemID = findViewById(R.id.txt_rentItemID);
                                    txtItemName = findViewById(R.id.txt_rentName);

                                    txtItemID.setText(itemId);
                                    txtItemName.setText(itemName);

//                                    Toast.makeText(DoneActivity.this, "ok4", Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RentActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

    private void insertRentData() {

        String rent_user = txtUser.getText().toString();
        String rent_department = txtDep.getText().toString();
        String rent_end = txtDate.getText().toString();
        String rent_item = item_id;
        String rent_type = type;

        StringRequest request = new StringRequest(Request.Method.POST, urlrent,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(RentActivity.this, "借物成功", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RentActivity.this, "err" + e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rent_end", rent_end);
                params.put("rent_user", rent_user);
                params.put("rent_department", rent_department);
                params.put("rent_item", rent_item);
                params.put("rent_type", rent_type);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RentActivity.this);
        requestQueue.add(request);

    }

}