package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.webinarapp.API.APIService;
import com.company.webinarapp.API.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText emailView;
    private EditText passwordView;
    private Button loginButton;
    private TextView lblregisterView;
    private APIService apiService;
    private static String token ="Bearer ";
    private static String Rol;
    private static final String org="ROLE_ORG";
    private static final String user="ROLE_USER";
    private static final String admin="ROLE_ADMIN";
    int contador=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    //    token = getIntent().getStringExtra("token");
       // Rol= getIntent().getStringExtra("rol");

        apiService = ApiUtils.getAPIService();

        emailView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        lblregisterView = (TextView) findViewById(R.id.lblRegister);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.GONE);

        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Login(apiService);

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


    private void verificacion()
    {
        if((Rol == org|| (Rol != null && Rol.equals(org))) || Rol == admin|| (Rol != null && Rol.equals(admin))) {
            Intent intent = new Intent(LoginActivity.this, HomeOrganizerActivity.class);

          //  String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
            intent.putExtra("token", token);
            intent.putExtra("rol", Rol);
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

    public void Login(APIService apiService)
    {
        String user= emailView.getText().toString();
        String pass= passwordView.getText().toString();
        if(!user.isEmpty() && !pass.isEmpty())
        {
            Auth_User(apiService,user,pass);
         //   Toast.makeText(this,"email: "+user,Toast.LENGTH_SHORT).show();
            Log.d("Login","email: "+user);
        }
        else
            {
                Toast.makeText(this,"Debes de llenar todos los campos",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

    }

    public void Auth_User(APIService apiService, String user, String pass)
    {
        Map<String,String> userAndPwd = new HashMap<>();
        userAndPwd.put("email",user);
        userAndPwd.put("password",pass);
        apiService.loginUser(userAndPwd).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Autenticacion correcta", Toast.LENGTH_SHORT).show();

                    String jspn = null;
                    try {
                        jspn = response.body().string();
                        Log.d("Logeo",jspn);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject t_usuario= new JSONObject(jspn);
                        token +=t_usuario.getString("token");

                        JSONObject user=new JSONObject(t_usuario.getString("user"));

                        JSONArray roles_json= user.getJSONArray("authorities");
                        JSONObject rol_json= roles_json.getJSONObject(0);
                        Rol=rol_json.getString("authority");

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    Log.d("MyApp",token);
                    Log.d("MyApp _Rol",Rol);

                    verificacion();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Logeo_user","Error"+(t.getMessage()));
                progressBar.setVisibility(View.GONE);

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