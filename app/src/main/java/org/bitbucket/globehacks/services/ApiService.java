package org.bitbucket.globehacks.services;

import org.bitbucket.globehacks.models.GeoPoint;
import org.bitbucket.globehacks.models.Store;
import org.bitbucket.globehacks.models.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Emmanuel Victor Garcia on 7/21/17.
 */

public interface ApiService {

    @Headers("Content-Type:application/json")
    @POST("{app_id}/{rest_key}/users/register")
    Observable<User> register(@Path("app_id") String app_id, @Path("rest_key") String rest_key, @Body User user);

    @Headers("Content-Type:application/json")
    @POST("{app_id}/{rest_key}/users/login")
    Observable<User> login(@Path("app_id") String app_id, @Path("rest_key") String rest_key, @Body User user);

    @GET("{app_id}/{rest_key}/users/isvalidusertoken/{token}")
    Observable<Boolean> validateSession(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                                        @Path("token") String token);

    @Headers("Content-Type:application/json")
    @PUT("{app_id}/{rest_key}/users/{object_id}")
    Observable<User> updateUser(@Header("user-token") String userToken, @Path("app_id") String app_id,
                                @Path("rest_key") String rest_key, @Path("object_id") String objectId, @Body User user);

    @GET("{app_id}/{rest_key}/users/logout")
    Observable<Boolean> logout(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                               @Header("user-token") String token);

    @Headers("Content-Type:application/json")
    @POST("{app_id}/{rest_key}/data/Store")
    Observable<Store> putStore(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                               @Header("user-token") String token, @Body Store store);

    @GET("{app_id}/{rest_key}/data/Store/{object_id}")
    Observable<Store> getStore(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                               @Header("user-token") String token, @Path("object_id") String objectId);

    @GET("{app_id}/{rest_key}/data/Store")
    Observable<List<Store>> getStores(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                                @Header("user-token") String token, @Query("where") String query);

    @Headers("Content-Type:application/json")
    @POST("{app_id}/{rest_key}/geo/points")
    Observable<GeoPoint> putGeoPoint(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                                     @Header("user-token") String token, @Body GeoPoint geoPoint);

    @GET("{app_id}/{rest_key}/geo/points")
    Observable<List<GeoPoint>> getGeoPoints(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                                            @Header("user-token") String token, @Query("nwlat") double nwLatitude,
                                            @Query("nwlon") double nwLongitude, @Query("selat") double seLatitude,
                                            @Query("selon") double seLongitude, @Query("pagesize") int pageSize,
                                            @Query("includemetadata") boolean meta);

}