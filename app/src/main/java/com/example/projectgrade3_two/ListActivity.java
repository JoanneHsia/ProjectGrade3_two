package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    String urlitemDone = "https://projectgrade3two.000webhostapp.com/doneItem.php";

    TableLayout doneList;

    String item_class,item_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//全螢幕顯示

        // | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵

//        Bundle bundle =  getIntent().getExtras();
//        item_class = bundle.getString("item_class");
//        String item_status = "閒置中";
//
//        itemDoneClass(item_class, item_status);

    }

    private void itemDoneClass(String item_class, String item_status){
        doneList = findViewById(R.id.tb_list);
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

                                    TableRow trDone = new TableRow(ListActivity.this);
                                    trDone.setLayoutParams(row_layout);
                                    trDone.setGravity(Gravity.CENTER_HORIZONTAL);

                                    TextView itemId = new TextView(ListActivity.this);
                                    itemId.setText(object.getString("item_id").trim());
                                    itemId.setLayoutParams(view_layout);

                                    TextView itemName = new TextView(ListActivity.this);
                                    itemName.setText(object.getString("item_name").trim());
                                    itemName.setLayoutParams(view_layout);

                                    TextView itemStatus = new TextView(ListActivity.this);
                                    itemStatus.setText(object.getString("item_status").trim());
                                    itemStatus.setLayoutParams(view_layout);

                                    TextView itemDescribe = new TextView(ListActivity.this);
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
                            Toast.makeText(ListActivity.this, "err", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_class", item_class);
                params.put("item_status", item_status);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }
}