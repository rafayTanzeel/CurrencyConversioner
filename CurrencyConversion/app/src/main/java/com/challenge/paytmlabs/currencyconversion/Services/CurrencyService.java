package com.challenge.paytmlabs.currencyconversion.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.challenge.paytmlabs.currencyconversion.Network.WebService;
import com.challenge.paytmlabs.currencyconversion.R;

/**
 * Provides an intent service to retrieve new currency information from the internet
 * @author Rafay Tanzzel
 */

public class CurrencyService extends IntentService {


    /**
     * Constructor sets up the name of the Service
     */
    public CurrencyService() {
        super("Currency_Service");
    }

    /**
     * Method is called on receiving an intent and sends a request to the baseurl
     * to retrieve new currency values
     * @param intent This takes an intent as an argument
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String base_url = getResources().getString(R.string.base_url_currency);
        WebService ws = new WebService(base_url);
        ws.sendRequest();
        runServiceByAlarm();
    }

    /**
     * Service sends itself a pending intent through Alarm Manager every 30 minutes
     * which triggers onHandleIntent and retrieves new currency values by sending a GET request
     */
    public void runServiceByAlarm(){
        AlarmManager mgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), CurrencyService.class);

        PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mgr.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent
        );
    }
}
