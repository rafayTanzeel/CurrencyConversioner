package com.challenge.paytmlabs.currencyconversion.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Provides an interface to establish a get request with query params with retrofit api
 * @author Rafay Tanzzel
 */

public interface WebInterface {
    @GET("latest")
    Call<WebModel> listCurrency(@Query("base") String currency);
}
