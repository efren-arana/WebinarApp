package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeOrganizerActivity extends AppCompatActivity implements RecyclerAdapter.RecyclerItemClick, SearchView.OnQueryTextListener {

    private RecyclerView rvLista;
    private SearchView svSearch;
    private RecyclerAdapter adapter;
    private List<ItemList> items = null;
    private ProgressBar progressBar;
    private static String token;
    private static String Rol;
    private static String ing_mod;
    private APIService apiService;
    List<ItemList> itemLists = new ArrayList<>();
    private ArrayList<Webinar> webinars = null;
    Handler handler = new Handler();
    int contador = 0;

    private static int LAUNCH_DETAIL = 1;
    private static int LAUNCH_MAIN_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_organizer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        token = getIntent().getStringExtra("token");
        Rol = getIntent().getStringExtra("rol");
        apiService = ApiUtils.getAPIService();


        initViews();
        initValues(apiService);
        initListener();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initViews() {
        rvLista = findViewById(R.id.rvLista);
        svSearch = findViewById(R.id.svSearch);
        progressBar=findViewById(R.id.progressBar_home_org);

    }

    private void initValues(APIService apiService) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);
        ListarWebinar(apiService);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                llenar();
                progressBar.setVisibility(View.GONE);
            }
        }, 5000);


    }


    private void llenar() {
        items = getItems();
        if (items != null) {
            adapter = new RecyclerAdapter(items, this);
            rvLista.setAdapter(adapter);


        }
    }

    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }

    private List<ItemList> getItems() {
        List<ItemList> itemLists = new ArrayList<>();

        if (webinars != null) {
            for (Webinar w : webinars) {

                itemLists.add(new ItemList(w.getId(), w.getTitle(), w.getDescription(), R.drawable.img_base));
                Log.d("MyApp", "for de webinar ");
            }
        } else {
            Log.d("MyApp", "No entre al for de webinar ");
        }

        return itemLists;
    }

    @Override
    public void itemClick(ItemList item) {

        Intent intent = new Intent(this, DetailEditActivity.class);
        intent.putExtra("itemDetail", item);
        intent.putExtra("token", token);
        intent.putExtra("rol", Rol);
        startActivity(intent);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_webinar:
                ing_mod = "INGRESAR";
                Intent intent = new Intent(HomeOrganizerActivity.this, MainActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("ing_mod", ing_mod);
                startActivity(intent);
                finish();
                return true;
            case R.id.logout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ListarWebinar(APIService apiService) {
        apiService.listerWebinar().enqueue(new Callback<ListadoWebinar>() {
            @Override
            public void onResponse(Call<ListadoWebinar> call, Response<ListadoWebinar> response) {
                if (response.isSuccessful()) {
                    ListadoWebinar lweb = response.body();
                    webinars = lweb.getWebinars();
                    Log.d("MyApp", "Toy dentro : " + webinars.size());
                    Log.d("prueba", "Titulo: " + webinars.get(0).getTitle());

                } else {
                    Toast.makeText(HomeOrganizerActivity.this, "No enviado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListadoWebinar> call, Throwable t) {
                Log.d("MyApp", "Error de consulta : " + t.getMessage());

            }
        });

    }


    @Override
    public void onBackPressed() {

        if (contador == 0) {
            Toast.makeText(HomeOrganizerActivity.this, "Presione nuevamente para salir de la aplicacion", Toast.LENGTH_SHORT).show();
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