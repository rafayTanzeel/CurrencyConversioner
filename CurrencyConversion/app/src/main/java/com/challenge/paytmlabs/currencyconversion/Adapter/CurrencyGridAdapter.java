package com.challenge.paytmlabs.currencyconversion.Adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.challenge.paytmlabs.currencyconversion.Database.Realm.CurrencyModel;
import com.challenge.paytmlabs.currencyconversion.R;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Provides functionality of Realm Recycler view adapter and view holder
 * This class includes additional functionality to detect on changing input text or spinner selection
 * and dynamically updates the value on the UI without making any changes in the realm database
 * @author Rafay Tanzzel
 */
public class CurrencyGridAdapter extends RealmRecyclerViewAdapter<CurrencyModel, CurrencyGridAdapter.CurrencyHolder>{

    public EditText editAmount;
    public Spinner dropdown;
    public double editRate;
    public double dbCMRate;
    private final String rateFormat = "%2.2e";


    public CurrencyGridAdapter(@Nullable OrderedRealmCollection<CurrencyModel> data, boolean autoUpdate, boolean updateOnModification, EditText editext, Spinner dropdown, double editRate, double dbCMRate) {
        super(data, autoUpdate, updateOnModification);
        this.editAmount = editext;
        this.dropdown = dropdown;
        this.editRate = editRate;
        this.dbCMRate = dbCMRate;
    }

    @Override
    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View view = lf.inflate(R.layout.currency_item, parent, false);
        return new CurrencyHolder(view);
    }

    @Override
    public void onBindViewHolder(final CurrencyHolder holder, int position) {
        final CurrencyModel curr_item = getData().get(position);

        if(!editAmount.getText().toString().isEmpty()){
            editRate = parseStringToDouble(editAmount.getText().toString());
        }

        holder.setRate(String.format(rateFormat,curr_item.getRate() * (editRate / dbCMRate)));
        holder.setCurrency(curr_item.getCurrency());

        textWatcherListener(holder, curr_item);
        spinnerUpdateListener(holder, curr_item);

    }


    /**
     * An event listener that detects any change in edit text selection and modifies values on the recycler view
     * without any changes to the database
     * @param holder View Holder for the binded view
     * @param curr_item A CurrencyModel bundle for currency and rate values
     */
    private void textWatcherListener(final CurrencyHolder holder, final CurrencyModel curr_item){
        // Detect text edit
        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    editRate = parseStringToDouble(editable.toString());
                }else{
                    editRate = 1.0;
                }

                holder.setRate(String.format(rateFormat, curr_item.getRate() * (editRate / dbCMRate)));
            }
        });
    }

    /**
     * A event listener that detects any change in spinner selection and modifies values on the recycler view
     * without any changes to the database
     * @param holder View Holder for the binded view
     * @param curr_item A CurrencyModel bundle for currency and rate values
     */
    private void spinnerUpdateListener(final CurrencyHolder holder, final CurrencyModel curr_item){
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCurrency = adapterView.getItemAtPosition(i).toString();

                Realm realm = Realm.getDefaultInstance();
                CurrencyModel cm = realm.where(CurrencyModel.class).equalTo("currency", selectedCurrency).findFirst();
                dbCMRate = cm.getRate();
                holder.setRate(String.format(rateFormat, curr_item.getRate() * (editRate / dbCMRate)));
                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private double parseStringToDouble(String value){
        double result = 1.0;
        if(value.compareTo(".")==0){
            result = 1.0;
        }else {
            try {
                result = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                result = 1.0;
            }
        }
        return result;
    }

    public class CurrencyHolder extends RecyclerView.ViewHolder{
        private TextView rate;
        private TextView currency;

        public CurrencyHolder(View itemView) {
            super(itemView);
            rate = itemView.findViewById(R.id.rate_item);
            currency = itemView.findViewById(R.id.currency_item);
        }

        public void setRate(String rate) {
            this.rate.setText(rate);
        }

        public void setCurrency(String currency) {
            this.currency.setText(currency);
        }
    }

}
