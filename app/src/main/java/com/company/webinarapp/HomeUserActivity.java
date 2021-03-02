package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.webinarapp.API.APIService;
import com.company.webinarapp.API.ApiUtils;
import com.company.webinarapp.DAO.ListadoWebinar;
import com.company.webinarapp.DAO.Webinar;
import com.company.webinarapp.adaptador.RecyclerAdapter;
import com.company.webinarapp.model.ItemList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeUserActivity extends AppCompatActivity implements RecyclerAdapter.RecyclerItemClick, SearchView.OnQueryTextListener {
    private RecyclerView rvLista;
    private SearchView svSearch;
    private RecyclerAdapter adapter;
    private List<ItemList> items;
    private APIService apiService;
    private static String token;
    private static String Rol;
    Handler handler = new Handler();
    private ArrayList<Webinar> webinars=null;
    private ProgressBar progressBar_user;
    int contador=0;
    private Button btn_actualizar,btn_m_calificados;

    private static final String org="ROLE_ORG";
    private static final String user="ROLE_USER";
    private static final String admin="ROLE_ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        apiService = ApiUtils.getAPIService();
        token = getIntent().getStringExtra("token");
        Rol = getIntent().getStringExtra("rol");
        setSupportActionBar(toolbar);
        initViews(apiService);
        initValues();
        initListener();
        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_user.setVisibility(View.VISIBLE);
                items=null;
                ListarWebinar(apiService);
            }
        });
    }
    private void initViews(APIService apiService){
        rvLista = findViewById(R.id.rvLista);
        svSearch = findViewById(R.id.svSearch);
        progressBar_user=(ProgressBar)findViewById(R.id.progressBar_user);
        btn_actualizar=(Button)findViewById(R.id.btn_actualizar_user);
        btn_m_calificados=(Button)findViewById(R.id.btn_mejor_calificados);
    }

    private void initValues() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);
        ListarWebinar(apiService);

        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                llenar();
            }
        },2000);*/

    }


    private void llenar()
    {
        items = getItems();
        if(items!=null) {
            adapter = new RecyclerAdapter(items, this);
            rvLista.setAdapter(adapter);

        }
    }

    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }

    private List<ItemList> getItems() {


        List<ItemList> itemLists = new ArrayList<>();

        if (webinars!=null) {
            for (Webinar w : webinars) {
                String fitro="ACEPTADO";
                if(fitro == w.getStatusWebinar() || (w.getStatusWebinar() != null && w.getStatusWebinar().equals(fitro))){
                itemLists.add(new ItemList(w.getId(),w.getTitle(), w.getDescription(), R.drawable.img_base));
                Log.d("MyApp","for de webinar "+fitro);
                }
            }
        }
        else
        {
            Log.d("MyApp","No entre al for de webinar ");
        }

        return itemLists;
    }

    @Override
    public void itemClick(ItemList item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("itemDetail", item);
        intent.putExtra("token",token);
        intent.putExtra("rol",Rol);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        if(items!=null) {
            adapter.filter(newText);
        }
        return false;
    }


    private void ListarWebinar(APIService apiService)
    {
        apiService.listerWebinar().enqueue(new Callback<ListadoWebinar>() {
            @Override
            public void onResponse(Call<ListadoWebinar> call, Response<ListadoWebinar> response) {
                if(response.isSuccessful()) {
                    ListadoWebinar lweb = response.body();
                    webinars = lweb.getWebinars();

                    progressBar_user.setVisibility(View.GONE);
                    Log.d("MyApp","Toy dentro : "+webinars.size());
                    Log.d("prueba","Titulo: "+webinars.get(0).getTitle());

                    llenar();
                }
                else
                {
                    Toast.makeText(HomeUserActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                    progressBar_user.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ListadoWebinar> call, Throwable t) {
                Log.d("MyApp","Error de consulta : "+t.getMessage());
                progressBar_user.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onBackPressed() {

        if (contador == 0) {
            Toast.makeText(HomeUserActivity.this, "Presione nuevamente para salir de la aplicacion", Toast.LENGTH_SHORT).show();
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