package com.example.projectgrade3_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
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

public class TodoListActivity extends AppCompatActivity {

    String item_class;

    String urlitemUndo = "https://projectgrade3two.000webhostapp.com/itemClass.php";
    String urlitemDone = "https://projectgrade3two.000webhostapp.com/itemClass.php";
    TableLayout undoList, doneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Bundle bundle =  getIntent().getExtras();
        item_class = bundle.getString("item_class");


    }


}