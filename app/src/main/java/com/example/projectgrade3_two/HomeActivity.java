package com.example.projectgrade3_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    TextView txtUserID, txtHosID;
    String user_id;

    String urllogin = "https://projectgrade3two.000webhostapp.com/userprofile.php";

    private Button btn_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle =  getIntent().getExtras();
        user_id = bundle.getString("user_id");
        login(user_id);

        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //全螢幕顯示

                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵

        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });
    }

//        final TextView text_view = (TextView) findViewById(R.id.tx_db);

//        new Thread(new Runnable(){
//            @Override
//            public void run(){
//                MysqlCon con = new MysqlCon();
//                con.run();
//                final String data = con.getData();
//                Log.v("OK",data);
//                text_view.post(new Runnable() {
//                    public void run() {
//                        text_view.setText(data);
//                    }
//                });
//
//            }
//        }).start();
private void login(String user_id){

    StringRequest request = new StringRequest(Request.Method.POST, urllogin,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("userProfile");

                        if (success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String userId = object.getString("user_id").trim();
                                String hosId = object.getString("hos_department").trim();

                                txtUserID = findViewById(R.id.txt_userID);
                                txtHosID = findViewById(R.id.txt_hosID);

                                txtUserID.setText(userId);
                                txtHosID.setText(hosId);

                                Toast.makeText(HomeActivity.this, "ok2", Toast.LENGTH_SHORT).show();



                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(HomeActivity.this, "err", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
    ){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);

            return params;
        }
    };

    Volley.newRequestQueue(this).add(request);

}


}