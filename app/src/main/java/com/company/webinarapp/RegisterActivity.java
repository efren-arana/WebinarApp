package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.company.webinarapp.API.APIService;
import com.company.webinarapp.API.ApiUtils;
import com.company.webinarapp.DAO.Roles;
import com.company.webinarapp.DAO.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextView lblloginView;
    private TextView genderText,titulo;
    private EditText nombre, email, password;
    private Spinner dropdown;
    private APIService apiService;
    private Button guardar_btn;
    private static String token="Bearer ";
    private static String Rol="no hay nada";


    Usuario Usuario;
   // ArrayList <Roles> roles = new ArrayList<Roles>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombre = (EditText) findViewById(R.id.nameregister);
        email = (EditText) findViewById(R.id.emailregister);
        password = (EditText) findViewById(R.id.passwordregister);
        titulo=(TextView)findViewById(R.id.lbl_titulo);
        lblloginView = (TextView) findViewById(R.id.lblLogin);

        apiService = ApiUtils.getAPIService();
        lblloginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("rol",Rol);
                startActivity(intent);
                finish();
            }
        });

        guardar_btn=(Button)findViewById(R.id.save_webinar_button2);
        guardar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = nombre.getText().toString();
                String ema = email.getText().toString();

                String pass = password.getText().toString();

                if(!nom.isEmpty() && !ema.isEmpty()  && !pass.isEmpty())
                {

                    ArrayList<Roles> roles = cargarRoles();

                    if(roles.size()!=0) {
                        Usuario Usuario = new Usuario();
                        Usuario.setEmail(ema);
                        Usuario.setEnable(true);
                        Usuario.setFulName(nom);
                        Usuario.setPassword(pass);
                        Usuario.setRoles(roles);

                        sendPost(Usuario,apiService);

                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dropdown = (Spinner) findViewById(R.id.spinner_register);
        genderText = (TextView) findViewById(R.id.gender_spinner);
        String[] items = new String[]{"Usuario", "Organizador"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.gender_spinner_row, R.id.gender_spinner, items);

        dropdown.setAdapter(adapter);


    }



    private void sendPost(Usuario Usuario,APIService apiService1)
    {

        apiService1.savePostUser(Usuario).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this, "Enviado con exito", Toast.LENGTH_SHORT).show();

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

                            JSONArray roles_json= user.getJSONArray("roles");
                            JSONObject rol_json= roles_json.getJSONObject(0);
                            Rol=rol_json.getString("authority");


                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    Log.d("MyApp",token);
                    Log.d("MyApp _Rol",Rol);




                }
                else
                    {
                        Toast.makeText(RegisterActivity.this, "No enviado", Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    titulo.setText(t.getMessage());
            }
        });

    }


    private ArrayList<Roles> cargarRoles() {

        ArrayList<Roles> roles = new ArrayList<Roles>();
        String txtroles = dropdown.getSelectedItem().toString();
        Roles rol = new Roles();

        switch (txtroles) {
            case "Usuario":
                rol.setId(1);
                rol.setAuthority("ROLES_USER");
                break;

            case "Organizador":
                rol.setId(2);
                rol.setAuthority("ROLE_ORG");
                break;

        }
        roles.add(rol);
        return roles;

    }
    @Override
    public void onBackPressed() {
        //String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
        Intent intent = new Intent(this, LoginActivity.class);
      //  intent.putExtra("token",t);
        startActivity(intent);
        finish();
    }



}