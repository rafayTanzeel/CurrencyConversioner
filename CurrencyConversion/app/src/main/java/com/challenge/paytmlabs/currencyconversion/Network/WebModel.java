package com.challenge.paytmlabs.currencyconversion.Network;

import java.util.Map;

/**
 * Provides an wrapper for parsing json as used by the Gson interface
 * @author Rafay Tanzzel
 */

public class WebModel {
    private String base;
    private String date;
    private Map<String, Object> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Object> getRates() {
        return rates;
    }

    public void setRates(Map<String, Object> rates) {
        this.rates = rates;
    }
}
