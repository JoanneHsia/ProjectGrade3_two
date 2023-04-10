package com.example.projectgrade3_two;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText emailText, passwordText;
    private Button btn_login;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().getDecorView().setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //隱藏狀態欄和標題欄

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//全螢幕顯示

                       // | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); //隱藏手機虛擬按鍵HOME/BACK/LIST按鍵


        btn_login = (Button) findViewById(R.id.btn_login);

        emailText = (EditText) findViewById(R.id.tx_email);
        passwordText = (EditText) findViewById(R.id.tx_password);

        mAuth =FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, user_id;
                user_id = emailText.getText().toString().trim();
                email = emailText.getText().toString() + "@fjuh.com";
                password = passwordText.getText().toString();

                if (email.isEmpty()){
                    emailText.setError("請填入帳號");
                    emailText.requestFocus();
                    return;
                }
                else if (password.isEmpty()) {
                    passwordText.setError("請輸入密碼");
                    passwordText.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", user_id);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return;
//                          startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this, "帳號或密碼有誤，請再試一次", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}