package com.challenge.paytmlabs.currencyconversion.Database.Realm;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Provides a schema for the realm database
 * @author Rafay Tanzzel
 */

public class CurrencyModel extends RealmObject {

    @PrimaryKey
    @Index
    private String currency;

    private double rate;

    public CurrencyModel() {}

    /**
     * Constructor used to initialize currency and rate values
     * @param currency_name
     * @param rate
     */
    public CurrencyModel(String currency_name, double rate) {
        this.currency = currency_name;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
