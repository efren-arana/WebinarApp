package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.company.webinarapp.API.APIService;
import com.company.webinarapp.API.ApiUtils;
import com.company.webinarapp.DAO.ObjetoWebinar;
import com.company.webinarapp.DAO.Webinar;
import com.company.webinarapp.model.ItemList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ImageView imgItemDetail;
    private TextView tvTituloDetail;
    private TextView tvDescripcionDetail;
    private ItemList itemDetail;
    private int id;
    private static String token;
    private static String Rol;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //setTitle(getClass().getSimpleName());
        setTitle(getString(R.string.title_activity_detail));

        token = getIntent().getStringExtra("token");
        Rol=getIntent().getStringExtra("rol");
        apiService = ApiUtils.getAPIService();

        itemDetail = (ItemList) getIntent().getExtras().getSerializable("itemDetail");
        id=itemDetail.getId();

        initViews();


        Log.d("token","E token de detalles: "+token + " el id : "+id);
        getIdWebinar(apiService);
    }

    private void initViews() {
        imgItemDetail = findViewById(R.id.imgItemDetail);
        tvTituloDetail = findViewById(R.id.tvTituloDetail);
        tvDescripcionDetail = findViewById(R.id.tvDescripcionDetail);
    }

    private void initValues(Webinar webinar){
        if (webinar!=null)
        {
            imgItemDetail.setImageResource(itemDetail.getImgResource());
            tvTituloDetail.setText(webinar.getTitle()+"  " +webinar.getId());
            tvDescripcionDetail.setText(webinar.getDescription());
        }

    }

    private void getIdWebinar(APIService apiService)
    {
        apiService.IdWebinar(id,token).enqueue(new Callback<ObjetoWebinar>() {
            @Override
            public void onResponse(Call<ObjetoWebinar> call, Response<ObjetoWebinar> response) {
                if (response.isSuccessful())
                {
                    ObjetoWebinar webinar =response.body();
                    Webinar web= webinar.getWebinars();

                    Log.d("MyApp","Toy dentro : ");
                    initValues(web);
                }
                else{
                    Log.d("MyApp","en el elsede respone ");
                }


            }

            @Override
            public void onFailure(Call<ObjetoWebinar> call, Throwable t) {
                Log.d("MyApp","Error de envio : "+t.getMessage());
            }
        });
    }

    public void redirigir_home()
    {
        String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
        Intent intent = new Intent(this, HomeOrganizerActivity.class);
        intent.putExtra("token",t);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        redirigir_home();
    }
}