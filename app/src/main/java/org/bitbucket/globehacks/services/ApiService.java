package org.bitbucket.globehacks.services;

import org.bitbucket.globehacks.models.Store;
import org.bitbucket.globehacks.models.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    Observable<Boolean> validateSession(@Path("app_id") String app_id, @Path("rest_key") String rest_key, @Path("token") String token);

    @GET("{app_id}/{rest_key}/users/logout")
    Observable<Boolean> logout(@Path("app_id") String app_id, @Path("rest_key") String rest_key, @Header("user-token") String token);

    @Headers("Content-Type:application/json")
    @POST("{app_id}/{rest_key}/data/Store")
    Observable<Store> putStore(@Path("app_id") String app_id, @Path("rest_key") String rest_key,
                               @Header("user-token") String token, @Body Store store);

}