package com.company.webinarapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.company.webinarapp.API.APIService;
import com.company.webinarapp.API.ApiUtils;
import com.company.webinarapp.DAO.ObjetoWebinar;
import com.company.webinarapp.DAO.Webinar;
import com.company.webinarapp.model.ItemList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEditActivity extends AppCompatActivity {
    private ImageView imgItemDetail;
    private TextView tvTituloDetail;
    private TextView tvDescripcionDetail,tvExpositor,tvFechaInicio,tvFechaFinal,tvEstadoWebinar,tvEmai,tvEnlace;
    private ItemList itemDetail;
    private Spinner spinner;
    private Button btn_eliminar,btn_editar,btn_camb_estado;
    private  static int id;
    private static String token;
    private static String Rol;
    private static String ing_mod;
    private APIService apiService;
    private  static  final  String DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    private  static  final  String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss";
    private  static  final  String DATE_FORMAT_3 = "yyyy-MM-dd";
    private  static  final  String DATE_FORMAT_2 = "HH:mm:ss";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        token = getIntent().getStringExtra("token");
        Rol=getIntent().getStringExtra("rol");
        apiService = ApiUtils.getAPIService();
        //setTitle(getClass().getSimpleName());
        setTitle(getString(R.string.title_activity_detail_edit));
        itemDetail = (ItemList) getIntent().getExtras().getSerializable("itemDetail");
        id=itemDetail.getId();
        initViews();

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inactivar_btn();

                EliminarWebinar(apiService);


            }
        });

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ing_mod="MODIFICAR";
                Intent intent=new Intent(DetailEditActivity.this,MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("ing_mod",ing_mod);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();


            }
        });

        Log.d("token","E token de detalles: "+token + " el id : "+id);
        getIdWebinar(apiService);

        String[] items = new String[]{"RECHAZADO","ACEPTADO","REGISTRADO","REVISANDO","CADUCADO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.gender_spinner_row, R.id.gender_spinner, items);

        spinner.setAdapter(adapter);

        btn_camb_estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inactivar_btn();
                StatusWebinar(apiService);


            }
        });
    }

    private void initViews() {
        imgItemDetail = findViewById(R.id.imgItemDetail);
        tvTituloDetail = findViewById(R.id.tvTituloDetail);
        tvDescripcionDetail = findViewById(R.id.tvDescripcionDetail);
        btn_eliminar=(Button)findViewById(R.id.eliminar_webinar_button);
        btn_editar=(Button)findViewById(R.id.editar_webinar_button);
        tvExpositor=(TextView)findViewById(R.id.lbl_getExpositor);
        tvFechaInicio=(TextView)findViewById(R.id.lbl_get_f_inicio);
        tvFechaFinal=(TextView)findViewById(R.id.lbl_get_f_final);
        tvEstadoWebinar=(TextView)findViewById(R.id.lbl_getEstado);
        spinner=(Spinner)findViewById(R.id.spin_estado);
        btn_camb_estado=(Button)findViewById(R.id.btn_estado);
        tvEnlace=(TextView)findViewById(R.id.lbl_getEnlace);

    }


    private void inactivar_btn()
    {
        btn_eliminar.setEnabled(false);
        btn_camb_estado.setEnabled(false);
        btn_editar.setEnabled(false);
    }

    private void activar_btn()
    {
        btn_eliminar.setEnabled(true);
        btn_camb_estado.setEnabled(true);
        btn_editar.setEnabled(true);
    }

    public String Formato(String date,String Formato_OUT,String Format_INT) {

        String str = null;
        try {
            str = formatDateFromDateString(Format_INT, Formato_OUT, date);
            Log.d("MyApp", "HOla :  " + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("MyApp", "HOla :  " + str);
        return str;
    }

    public static String formatDateFromDateString(String inputDateFormat, String outputDateFormat,String inputDate) throws ParseException
    {
        Date mParsedDate;
        String mOutputDateString;
        SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputDateFormat, java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputDateFormat, java.util.Locale.getDefault());
        mParsedDate = mInputDateFormat.parse(inputDate);
        mOutputDateString = mOutputDateFormat.format(mParsedDate);
        return mOutputDateString;
    }

    private void initValues(Webinar webinar){
        if (webinar!=null)
        {
            imgItemDetail.setImageResource(itemDetail.getImgResource());
            tvTituloDetail.setText(webinar.getTitle()+"  " +webinar.getId());
            tvDescripcionDetail.setText(webinar.getDescription());
            tvFechaInicio.setText(Formato(webinar.getStartDate(),DATE_FORMAT_8,DATE_FORMAT_12));
            tvFechaFinal.setText(Formato(webinar.getFinishDate(),DATE_FORMAT_8,DATE_FORMAT_12));
            tvExpositor.setText(webinar.getNameExpositor());
            tvEstadoWebinar.setText(webinar.getStatusWebinar());
            tvEnlace.setText(webinar.getUrlWebinar());
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

    private void EliminarWebinar(APIService apiService)
    {
      //  String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
        apiService.DeleteWebinar(id,token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful())
                {
                    Log.d("Delete","Eliminado correctamente ");
                    activar_btn();
                    redirigir_home();
                }
                else
                    {
                        Log.d("delete","Mal enviado ");
                        activar_btn();
                    }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("delete","Error de envio :"+t.getMessage());
                activar_btn();
            }
        });
    }



    public void redirigir_home()
    {
       // String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
        Intent intent = new Intent(this, HomeOrganizerActivity.class);
        intent.putExtra("token",token);
        startActivity(intent);
        finish();
    }


    private void StatusWebinar(APIService apiService)

    {
        String estado= spinner.getSelectedItem().toString();
        apiService.StatusWebinar(id,token,estado).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful())
                {
                    redirigir_home();
                    activar_btn();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Status Web","error: "+t.getMessage());
                activar_btn();

            }
        });
    }

    @Override
    public void onBackPressed() {
        redirigir_home();
    }



}