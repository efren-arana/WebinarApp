package com.company.webinarapp.API;

import com.company.webinarapp.DAO.CalificacionComentario;
import com.company.webinarapp.DAO.ListadoWebinar;
import com.company.webinarapp.DAO.ObjetoWebinar;
import com.company.webinarapp.DAO.Usuario;
import com.company.webinarapp.DAO.Webinar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("auth/user/registration")
    Call<ResponseBody> savePostUser(@Body Usuario user);


    @POST("webinar/create")
    Call<ResponseBody> savePostWebinar(@Body Webinar webinar, @Header("Authorization") String token);

    @GET("webinar/listar")
    Call<ListadoWebinar> listerWebinar();

    @GET("webinar/read/{webinarId}")
    Call<ObjetoWebinar> IdWebinar(@Path("webinarId") int id, @Header("Authorization") String token);

    @PUT("webinar/setStatus/{webinarId}")
    Call<ResponseBody> StatusWebinar(@Path("webinarId") int id, @Header("Authorization") String token, @Query("statusWebinar") String status);

    @DELETE("webinar/delete/{webinarId}")
    Call<ResponseBody> DeleteWebinar(@Path("webinarId") int id, @Header("Authorization") String token);

    @PUT("webinar/update/{webinarId}")
    Call<ResponseBody> UpdateWebinar(@Body Webinar web, @Header("Authorization") String token,@Path("webinarId") int id);

    @POST("webinar/rate/{webinarId}")
    Call<ResponseBody> CalificaWebinar(@Path("webinarId") int id,@Header("Authorization") String token,@Body CalificacionComentario rating);

}
