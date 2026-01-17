package model.api;

import model.domain.Application;
import retrofit2.http.Query;

import java.util.List;

import model.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WorklinkApiInterface {

    // USERS *********************************************************************
    @GET("users")
    Call<List<User>> getUser();
    @GET("users")
    Call<List<User>> getUsersByEmail(@Query("email") String email);
    @POST("users")
    Call<User> registerUser (@Body User user);
    @GET("users/{id}")
    Call<User> getUser(@Path("id") long id);
    @PUT("users/{id}")
    Call<User> modifyUser(@Path("id") long id, @Body User user);
    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") long id);
    @GET("users/login")
    Call<User> loginUser(
            @Query("email") String email,
            @Query("password") String password
    );


    // APPLICATION *************************************************************
    @GET("application")
    Call<List<Application>> getApplication();
    @GET("application")
    Call<List<Application>> getApplicationByType(@Query("type") String type);
    @GET("/application/user/{userId}")
    Call<List<Application>> getApplicationsByUser(@Path("userId") Long userId);
    @POST("application")
    Call<Application> registerApplication (@Body Application application);
    @GET("application/{id}")
    Call<Application> getApplication(@Path("id") long id);
    @PUT("application/{id}")
    Call<Application> modifyApplication(@Path("id") long id, @Body Application application);
    @DELETE("application/{id}")
    Call<Void> deleteApplication(@Path("id") long id);


}
