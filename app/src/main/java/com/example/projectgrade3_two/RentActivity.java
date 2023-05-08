package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RentActivity extends AppCompatActivity {

    private Activity context=this;

    String urlitemid = "https://projectgrade3two.000webhostapp.com/searchItem.php";
    String urlrent = "https://projectgrade3two.000webhostapp.com/rent.php";

    String urlRepUpdate = "https://projectgrade3two.000webhostapp.com/updateStatus.php";

    Button btn_rent_send, btn_date;
    String item_id, user_id, type;

    TextView txtItemID, txtItemName, txtDate;

    EditText txtUser, txtDep;
    RadioGroup Group;

    DatePickerDialog.OnDateSetListener datePicker;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        txtUser = findViewById(R.id.ed_user);
        txtDep = findViewById(R.id.ed_department);

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
        Group = findViewById(R.id.Group);
        Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID){
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
                updateRent(item_id);
                Intent intent = new Intent(RentActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_date = findViewById(R.id.btn_date);
        txtDate = findViewById(R.id.txt_date);
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, date);
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                txtDate.setText(sdf.format(calendar.getTime()));

            }
        };
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(RentActivity.this,datePicker,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
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
//        String rent_department = txtDep.getText().toString();
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
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("rent_user", rent_user);
//                params.put("rent_department", rent_department);
                params.put("rent_end", rent_end);
                params.put("rent_item", rent_item);
                params.put("rent_type", rent_type);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RentActivity.this);
        requestQueue.add(request);

    }

    private void updateRent(String item_id) {

        String item_status = "借用中";

        StringRequest request = new StringRequest(Request.Method.POST, urlRepUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
//                                Toast.makeText(RentActivity.this, "登記成功", Toast.LENGTH_SHORT).show();

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
                params.put("item_status", item_status);
                params.put("item_id", item_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RentActivity.this);
        requestQueue.add(request);

    }

}