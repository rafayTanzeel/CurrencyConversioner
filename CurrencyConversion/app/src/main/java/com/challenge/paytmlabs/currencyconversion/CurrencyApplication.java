package com.challenge.paytmlabs.currencyconversion;

import android.app.Application;
import android.content.Intent;

import com.challenge.paytmlabs.currencyconversion.Services.CurrencyService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * The global configurations of establishing the realm database and intent service are setup here
 * @author Rafay Tanzzel
 */
public class CurrencyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("currency.realm")
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        Intent intent = new Intent(this, CurrencyService.class);
        startService(intent);

    }
}
