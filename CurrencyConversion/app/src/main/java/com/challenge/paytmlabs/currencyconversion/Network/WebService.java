package com.challenge.paytmlabs.currencyconversion.Network;

import com.challenge.paytmlabs.currencyconversion.Database.Realm.CurrencyModel;

import java.util.concurrent.Executors;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides an API to retrieve currency data from the internet and parse it
 * using Gson and storing it in realm database
 * @author Rafay Tanzzel
 */

public class WebService {

    Retrofit retrofit;

    /**
     * Builds the retrofit model using the base url as an arguement
     * @param base_url This url is used to retrieve and parse the json data
     */
    public WebService(String base_url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Provides the functionality to send a Get request and retrieve the json data.
     * The json data is further parsed and stored int the realm data base
     */
    public void sendRequest() {
        WebInterface service = retrofit.create(WebInterface.class);
        Call<WebModel> currency_items = service.listCurrency("USD");

        currency_items.enqueue(new Callback<WebModel>() {
            @Override
            public void onResponse(Call<WebModel> call, Response<WebModel> response) {
                if (response.isSuccessful()) {
                    WebModel currency_items = response.body();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(new CurrencyModel("USD", 1));
                    for (String property : currency_items.getRates().keySet()) {
                        final String currency = property;
                        final double rate = Double.parseDouble(currency_items.getRates().get(property).toString());
                        realm.copyToRealmOrUpdate(new CurrencyModel(currency, rate));
                    }
                    realm.commitTransaction();
                }
            }

            @Override
            public void onFailure(Call<WebModel> call, Throwable t) {}
        });
    }
}
