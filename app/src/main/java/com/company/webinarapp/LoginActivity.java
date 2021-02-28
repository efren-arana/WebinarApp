package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText passwordView;
    private Button loginButton;
    private TextView lblregisterView;
    private static String token;
    private static String Rol;
    private static final String org="ROLE_ORG";
    private static final String user="ROLE_USER";
    private static final String admin="ROLE_ADMIN";
    int contador=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        token = getIntent().getStringExtra("token");
        Rol= getIntent().getStringExtra("rol");

        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        lblregisterView = (TextView) findViewById(R.id.lblRegister);

        loginButton = (Button) findViewById(R.id.login_button);
        emailView.setText(Rol);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rol=org;
                if(Rol == org || (Rol != null && Rol.equals(org))) {
                    Intent intent = new Intent(LoginActivity.this, HomeOrganizerActivity.class);

                    String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";


                  /*  intent.putExtra("token", token);
                    intent.putExtra("rol", Rol);*/
                    intent.putExtra("token", t);
                    intent.putExtra("rol", org);
                    startActivity(intent);
                    finish();
                }
                else
                    {
                        Intent intent = new Intent(LoginActivity.this, HomeUserActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("rol", Rol);
                        startActivity(intent);
                        finish();
                    }
            }
        });
        lblregisterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (contador == 0) {
            Toast.makeText(LoginActivity.this, "Presione nuevamente para salir de la aplicacion", Toast.LENGTH_SHORT).show();
            contador++;
        } else {
            super.onBackPressed();

        }

        new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                contador=0;
            }
        }.start();
    }

}