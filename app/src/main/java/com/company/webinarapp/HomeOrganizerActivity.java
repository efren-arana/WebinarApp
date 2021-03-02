package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private String filtro="ACEPTADOS";
    private Button btn_aprobado,btn_registrado,btn_rechazado,btn_revisando,btn_caducado,btn_todos;
    private Button btn_ing_web_org,btn_actualizar;
    private LinearLayout botones_admin, botones_org;


    private static final String org="ROLE_ORG";
    private static final String user="ROLE_USER";
    private static final String admin="ROLE_ADMIN";
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



       // botones_admin.setVisibility(View.GONE);

        initViews();
        initValues(apiService);
        initListener();

        btn_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro="TODOS";
                items=null;
                llenar();
            }
        });

        btn_aprobado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro="ACEPTADO";
                items=null;
                llenar();

            }
        });


        btn_registrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro="REGISTRADO";
                items=null;
                llenar();

            }
        });

        btn_revisando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro="REVISANDO";
                items=null;
                llenar();
            }
        });

        btn_rechazado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro="RECHAZADO";
                items=null;
                llenar();
                Log.d("MyApp", "boton rechazado "+filtro+"  "+webinars.size());
            }
        });
        btn_caducado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtro="CADUCADO";
                items=null;
                llenar();
            }
        });

        btn_ing_web_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ing_mod = "INGRESAR";
                Intent intent = new Intent(HomeOrganizerActivity.this, MainActivity.class);
                intent.putExtra("rol",Rol);
                intent.putExtra("token", token);
                intent.putExtra("ing_mod", ing_mod);
                startActivity(intent);
                finish();

            }
        });

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                filtro="ACEPTADO";
                items=null;
               /* llenar();
                Log.d("MyApp", "boton rechazado "+filtro+"  "+webinars.size());*/
                ListarWebinar(apiService);
            }
        });




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
        btn_aprobado=(Button)findViewById(R.id.btn_aprobado);
        btn_caducado=(Button)findViewById(R.id.btn_caducado);
        btn_rechazado=(Button)findViewById(R.id.btn_rechazado);
        btn_revisando=(Button)findViewById(R.id.btn_revisando);
        btn_registrado=(Button)findViewById(R.id.btn_registrando);
        btn_todos=(Button)findViewById(R.id.btn_todos);
        botones_admin=(LinearLayout)findViewById(R.id.l_botones);
        botones_org =(LinearLayout)findViewById(R.id.l_organizador);
        btn_ing_web_org=(Button) findViewById(R.id.btn_ingresar_web_org);
        btn_actualizar=(Button)findViewById(R.id.btn_actualizar_org);

        if(Rol == admin || (Rol != null && Rol.equals(admin))) {
            botones_admin.setVisibility(View.VISIBLE);
            botones_org.setVisibility(View.GONE);
        }
        else {
            botones_org.setVisibility(View.VISIBLE);
            botones_admin.setVisibility(View.GONE);
        }


        Log.d("MyApp", "Rol de homeOrgna "+Rol);

    }

    private void initValues(APIService apiService) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);
        ListarWebinar(apiService);


    }


    private void llenar() {
        items = getItems(filtro);
        if (items != null) {
            adapter = new RecyclerAdapter(items, this);
            rvLista.setAdapter(adapter);


        }else
        {
            Log.d("MyApp", "NO se lleno items ");
        }
    }

    private void initListener() {
        svSearch.setOnQueryTextListener(this);
    }

    private List<ItemList> getItems(String Status) {
        List<ItemList> itemLists = new ArrayList<>();

        if (webinars != null) {
            for (Webinar w : webinars) {

                if(filtro == w.getStatusWebinar() || (w.getStatusWebinar() != null && w.getStatusWebinar().equals(filtro))) {
                    itemLists.add(new ItemList(w.getId(), w.getTitle(), w.getDescription(), R.drawable.img_base));
                    Log.d("MyApp", "for de webinar "+w.getStatusWebinar());
                }
                if(Status=="TODOS")
                {
                        itemLists.add(new ItemList(w.getId(), w.getTitle(), w.getDescription(), R.drawable.img_base));
                        Log.d("MyApp", "for de webinar "+w.getStatusWebinar());
                }
            }
        } else {
            Log.d("MyApp", "No entre al for de webinar : "+filtro);
        }

        return itemLists;
    }

    @Override
    public void itemClick(ItemList item) {

        if(Rol == admin || (Rol != null && Rol.equals(admin)))
        {
            Intent intent = new Intent(this, DetailEditActivity.class);
            intent.putExtra("itemDetail", item);
            intent.putExtra("token", token);
            intent.putExtra("rol", Rol);
            startActivity(intent);
            finish();
        }
        else
        {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("itemDetail", item);
                intent.putExtra("token",token);
                intent.putExtra("rol",Rol);
                startActivity(intent);
                finish();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(Rol == admin || (Rol != null && Rol.equals(admin))) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
            switch (item.getItemId()) {
                case R.id.add_webinar:
                    ing_mod = "INGRESAR";
                    Intent intent = new Intent(HomeOrganizerActivity.this, MainActivity.class);
                    intent.putExtra("rol",Rol);
                    intent.putExtra("token", token);
                    intent.putExtra("ing_mod", ing_mod);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.logout:
                    Log.d("MyApp", "Es la opcion de alado : " + filtro);
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
                    llenar();
                    progressBar.setVisibility(View.GONE);
                    Log.d("MyApp", "Toy dentro : " + webinars.size());
                    Log.d("prueba", "Titulo: " + webinars.get(0).getTitle());

                } else {
                    Toast.makeText(HomeOrganizerActivity.this, "Error en la consulta", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ListadoWebinar> call, Throwable t) {
                Log.d("MyApp", "Error de consulta : " + t.getMessage());
                progressBar.setVisibility(View.GONE);

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