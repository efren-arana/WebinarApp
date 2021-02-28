package com.company.webinarapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.company.webinarapp.API.APIService;
import com.company.webinarapp.API.ApiUtils;
import com.company.webinarapp.DAO.LocationWebinar;
import com.company.webinarapp.DAO.ObjetoWebinar;
import com.company.webinarapp.DAO.Webinar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button saveWebinarButton;
    Button ModificarWebinarButton, modificarWebinar, EliminarWebinar;
    int LAUNCH_SECOND_ACTIVITY = 1996;
    String coordX="", coordY="";

    Button imageButton;
    Button ubicationButton;
    Button fechabtn_inicio,horabtn_inicio,fechabtn_final,horabtn_final;
    TextView lbl_fecha_inicio, lbl_hora_inicio,lbl_fecha_final,lbl_hora_final;
    ImageView imgfoto;
    Bitmap bitmap=null;
    TextView lblimagen;
    EditText titulo,enlace,descripcion,ubicacion,txt_expo,txt_obs;
    private APIService apiService;

    int bandera=0;
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 1;
    private static final int COD_FOTO = 20;
    private  static  final  String DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    private  static  final  String DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss";
    private  static  final  String DATE_FORMAT_3 = "yyyy-MM-dd";
    private  static  final  String DATE_FORMAT_2 = "HH:mm:ss";
    //private LocationManager ubicacion;

    private static String token_recuperado="";
    private static String ing_mod;
    private static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token_recuperado = getIntent().getStringExtra("token");
        ing_mod=getIntent().getStringExtra("ing_mod");
        id=getIntent().getIntExtra("id",0);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        apiService = ApiUtils.getAPIService();


        lbl_fecha_inicio = (TextView)findViewById(R.id.campoFecha);
        lbl_hora_inicio = (TextView)findViewById(R.id.campoHora);
        lblimagen = (TextView)findViewById(R.id.lbl_imagen);

        titulo=(EditText)findViewById(R.id.title_webinar);
        descripcion=(EditText)findViewById(R.id.description_webinar);
        enlace=(EditText)findViewById(R.id.campoDoc);
        ubicacion=(EditText)findViewById(R.id.campoUbi);
        ubicacion.setEnabled(false);
        lbl_fecha_final=(TextView)findViewById(R.id.lbl_fechafinal);
        lbl_hora_final=(TextView)findViewById(R.id.lbl_hora_final);
        txt_expo=(EditText)findViewById(R.id.txt_expositor);
        txt_obs=(EditText)findViewById(R.id.txt_Observacion);

        fechabtn_final=(Button) findViewById(R.id.btn_fecha_final);

        fechabtn_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCalendario_final();
            }
        });



        horabtn_final=(Button)findViewById(R.id.btn_hora_final);
        horabtn_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHora_final();
            }
        });

        fechabtn_inicio = (Button)findViewById(R.id.fech_button);
        fechabtn_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCalendario_inicio();
            }
        });


        horabtn_inicio = (Button)findViewById(R.id.hora_button);
        horabtn_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirHora_inicio();
            }
        });


        imgfoto = (ImageView)findViewById(R.id.imgFoto);
        imgfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               act_band();



            }
        });

        ModificarWebinarButton = (Button) findViewById(R.id.modificar_webinar_button);
        ModificarWebinarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                modificacion(apiService);
            }
        });
        saveWebinarButton = (Button) findViewById(R.id.save_webinar_button);
        saveWebinarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               registrar(apiService);

            }
        });



        imageButton = (Button) findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });


        ubicationButton = (Button) findViewById(R.id.ubi_button);
        ubicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
            }
        });
        cargar_datos(apiService);

    }


    private void initValues(Webinar webinar){
        if (webinar!=null)
        {
            titulo.setText(webinar.getTitle());
            descripcion.setText(webinar.getDescription());
            enlace.setText(webinar.getUrlWebinar());
            txt_expo.setText(webinar.getNameExpositor());
            txt_obs.setText(webinar.getObservacion()+" ");

        }
    }

    private void cargar_datos(APIService apiService)
    {
        String str="MODIFICAR";
        String str2="INGRESAR";
        if(ing_mod == str || ing_mod != null && ing_mod.equals(str) && id!=0) {
            saveWebinarButton.setVisibility(View.GONE);
            getIdWebinar(apiService);
            Log.d("Mainactivity","Modificar Toy dentro del if cargar datos id="+id+" token = "+token_recuperado);
        }
        if(ing_mod == str2 || ing_mod != null && ing_mod.equals(str2) && id==0)
        {
            ModificarWebinarButton.setVisibility(View.GONE);
            Log.d("Mainactivity","Ingresar Toy dentro del if cargar datos id="+id+" token = "+token_recuperado);
        }

    }

    private void getIdWebinar(APIService apiService)
    {
        apiService.IdWebinar(id,token_recuperado).enqueue(new Callback<ObjetoWebinar>() {
            @Override
            public void onResponse(Call<ObjetoWebinar> call, Response<ObjetoWebinar> response) {
                if (response.isSuccessful())
                {
                    ObjetoWebinar webinar =response.body();
                    Webinar web= webinar.getWebinars();
                    Log.d("MyApp","Toy dentro de IdWebinar");
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


    private void act_band()
    {
        if(bitmap!=null) {
            String imagen = convertirImgString(bitmap);
            if (!imagen.isEmpty()) {
                Toast.makeText(this, "hay una imagen:  ", Toast.LENGTH_SHORT).show();
            }
            else
                {
                    Toast.makeText(this, "caso una imagen:  ", Toast.LENGTH_SHORT).show();
                }
        }
        else {
            Toast.makeText(this, "no hay una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirCalendario_inicio()
    {
        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd= new DatePickerDialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = year+"-"+month+"-"+day;
                        lbl_fecha_inicio.setText(Formato(date,DATE_FORMAT_3,"yyyy-mm-dd"));

                    }
                },año,mes,dia);
        dpd.show();
    }

    private void abrirHora_inicio()
    {
        Calendar calendar =Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog= new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                    String date = hour+":"+min;
                    lbl_hora_inicio.setText(Formato(date,DATE_FORMAT_2,"hh:mm"));
            }
        }, hora,minutos,true);
        timePickerDialog.show();

    }


    private void abrirCalendario_final()
    {
        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd= new DatePickerDialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = year+"-"+month+"-"+day;
                        lbl_fecha_final.setText(Formato(date,DATE_FORMAT_3,"yyyy-mm-dd"));

                    }
                },año,mes,dia);
        dpd.show();

    }

    private void abrirHora_final()
    {
        Calendar calendar =Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog= new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String date = hour+":"+min;
                lbl_hora_final.setText(Formato(date,DATE_FORMAT_2,"hh:mm"));
            }
        }, hora,minutos,true);
        timePickerDialog.show();

    }

    private String convertirImgString(Bitmap bitp)
    {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitp.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }



    private void mostrarDialogOpciones() {
        final CharSequence[] opciones={"Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //abriCamara();
                }else{
                    if (opciones[i].equals("Elegir de Galeria")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);

                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                coordX=data.getStringExtra("coordX");
                coordY=data.getStringExtra("coordY");
                ubicacion.setText("("+coordX+","+coordY+")");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
            return;
        }

        if (resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {

            Uri miPath=data.getData();
            imgfoto.setImageURI(miPath);

            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),miPath);
                imgfoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }



    private void registrar (APIService apiService)
    {

        String tit = titulo.getText().toString();
        String desc = descripcion.getText().toString();
        String enl = enlace.getText().toString();
       // String ubi = ubicacion.getText().toString();
        String dia_web_inicio=  lbl_fecha_inicio.getText().toString();
        String hora_web_inicio= lbl_hora_inicio.getText().toString();
        String dia_web_final = lbl_fecha_final.getText().toString();
        String hora_web_final = lbl_hora_final.getText().toString();
        String expositor=txt_obs.getText().toString();
        String observacion=txt_obs.getText().toString();

        String inicio= ConvertirDateTime(hora_web_inicio,dia_web_inicio);
        String fin = ConvertirDateTime(hora_web_final,dia_web_final);
        if(!tit.isEmpty() &&  !desc.isEmpty() && !enl.isEmpty() &&
                !dia_web_inicio.isEmpty() && !hora_web_inicio.isEmpty() && !dia_web_final.isEmpty()
                && !hora_web_final.isEmpty() && bitmap!=null && !expositor.isEmpty())
        {

            if(!token_recuperado.isEmpty()) {

                LocationWebinar locationWebinar = new LocationWebinar();
                locationWebinar.setLatitud(coordX);
                locationWebinar.setLongitud(coordY);


                Log.d("MyApp", "cordX:  " + coordX+" cordY"+coordY);

                Webinar webinar = new Webinar();
                webinar.setDescription(desc);
                webinar.setStartDate(inicio);
                webinar.setFinishDate(fin);
                webinar.setDuration(2);
                webinar.setNameExpositor(expositor);
                webinar.setObservacion(observacion);
                webinar.setTitle(tit);
                webinar.setImageUrl("convertirImgString(bitmap)");
                webinar.setUrlWebinar(enl);


                webinar.setLocationWebinar(locationWebinar);


                sendPostWebinar(webinar, apiService);


                //   Toast.makeText(this, "Registro guardado con exito", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Token no generado",Toast.LENGTH_SHORT).show();

            }
        }
        else{

            Toast.makeText(this,"Debes de llenar todos los campos",Toast.LENGTH_SHORT).show();
           // modificarWebinar.setEnabled(true);
        }

        //modificarWebinar.setEnabled(true);

    }

    private void sendPostWebinar(Webinar webinar, APIService apiService)
    {
        apiService.savePostWebinar(webinar,token_recuperado).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful())
                {

                    Toast.makeText(MainActivity.this, "Enviado con exito", Toast.LENGTH_SHORT).show();
                    redireccionar_mod();


                }
                else
                {

                    Toast.makeText(MainActivity.this, "No enviado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No enviado: "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private String ConvertirDateTime(String hora, String fecha1)
    {
        String f_completa=fecha1+" "+hora;

        String str=Formato(f_completa,DATE_FORMAT_12,DATE_FORMAT_8);

        return str;
    }



    public String Formato(String date,String Formato_OUT,String Format_INT) {

        String str = null;
        try {
            str = formatDateFromDateString(Format_INT, Formato_OUT, date);
           // Log.d("MyApp", "HOla :  " + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      //  Log.d("MyApp", "HOla :  " + str);
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





    public void modificacion (APIService apiService) {


        String tit = titulo.getText().toString();
        String desc = descripcion.getText().toString();
        String enl = enlace.getText().toString();
        String dia_web_inicio=  lbl_fecha_inicio.getText().toString();
        String hora_web_inicio= lbl_hora_inicio.getText().toString();
        String dia_web_final = lbl_fecha_final.getText().toString();
        String hora_web_final = lbl_hora_final.getText().toString();
        String expositor=txt_obs.getText().toString();
        String observacion=txt_obs.getText().toString();

        String inicio= ConvertirDateTime(hora_web_inicio,dia_web_inicio);
        String fin = ConvertirDateTime(hora_web_final,dia_web_final);
        if(!tit.isEmpty() &&  !desc.isEmpty() && !enl.isEmpty() && !dia_web_inicio.isEmpty()
                && !hora_web_inicio.isEmpty() && !dia_web_final.isEmpty()
                && !hora_web_final.isEmpty() && bitmap!=null && !expositor.isEmpty())
        {

            if(!token_recuperado.isEmpty()) {

                LocationWebinar locationWebinar = new LocationWebinar();
                locationWebinar.setLatitud(coordX);
                locationWebinar.setLatitud(coordY);
                Webinar webinar = new Webinar();
                webinar.setDescription(desc);
                webinar.setStartDate(inicio);
                webinar.setFinishDate(fin);
                webinar.setDuration(2);
                webinar.setNameExpositor(expositor);
                webinar.setObservacion(observacion);
                webinar.setTitle(tit);
                webinar.setImageUrl("convertirImgString(bitmap)");
                webinar.setUrlWebinar(enl);


                webinar.setLocationWebinar(locationWebinar);


               updateWebinarPut(apiService,webinar);


            }
            else {
                Toast.makeText(this,"Token no generado",Toast.LENGTH_SHORT).show();
                modificarWebinar.setEnabled(true);
            }
        }
        else{

            Toast.makeText(this,"Debes de llenar todos los campos",Toast.LENGTH_SHORT).show();
            modificarWebinar.setEnabled(true);
        }




    }


    public void updateWebinarPut(APIService apiService,Webinar webinar)
    {

        String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
        apiService.UpdateWebinar(webinar,t,id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful())
                {

                    Toast.makeText(MainActivity.this, "Enviado con exito", Toast.LENGTH_SHORT).show();
                    redireccionar_mod();

                }
                else
                {

                    Toast.makeText(MainActivity.this, "No enviado", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("Modificacion","Error envio "+t.getMessage());

            }
        });
    }

    private void redireccionar_mod()
    {
        String t="Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJpYXQiOjE2MTQyODQ2NDMsImV4cCI6MTYxNTQ5NDI0M30.295RSjkJOJFrmxWAD2i-hgSqjrXxBtkv3NAH9nSVwvY";
        Intent intent = new Intent(this, HomeOrganizerActivity.class);
        intent.putExtra("token",t);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        redireccionar_mod();
    }

}