package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RentListActivity extends AppCompatActivity {

    String user_id, rent_user, item_id;
    Button btn_back;
    TextView userId, userDep, itemName, itemId, date, type, start;

    String urlrentData = "https://projectgrade3two.000webhostapp.com/rentData.php";
    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_list);

        Bundle bundle =  getIntent().getExtras();
        user_id = bundle.getString("user_id");
        item_id = bundle.getString("item_id");
        rent_user = bundle.getString("rent_user");



        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //全螢幕顯示

                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵

        rentData(rent_user);
        itemQrcode(item_id);

        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentListActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void rentData(String rent_user){

        StringRequest request = new StringRequest(Request.Method.POST, urlrentData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("rentData");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String rentUser = object.getString("rent_user").trim();
                                    String rentHos = object.getString("rent_department").trim();
                                    String rentDate = object.getString("rent_end").trim();
                                    String rentType = object.getString("rent_type").trim();
                                    String rentStart = object.getString("rent_start").trim();

                                    userId = findViewById(R.id.rentList_user);
                                    userDep = findViewById(R.id.rentList_dep);
                                    date = findViewById(R.id.rentList_date);
                                    type = findViewById(R.id.rentList_type);
                                    start = findViewById(R.id.rentList_start);

                                    userId.setText(rentUser);
                                    userDep.setText(rentHos);
                                    date.setText(rentDate);
                                    type.setText(rentType);
                                    start.setText(rentStart);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RentListActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RentListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rent_user", rent_user);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

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

                                    String rentItem = object.getString("item_id").trim();
                                    String rentName = object.getString("item_name").trim();

                                    itemId = findViewById(R.id.rentList_itemID);
                                    itemName = findViewById(R.id.rentList_itemNmae);

                                    itemId.setText(rentItem);
                                    itemName.setText(rentName);

//                                    Toast.makeText(DoneActivity.this, "ok4", Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RentListActivity.this, "err", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RentListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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

}