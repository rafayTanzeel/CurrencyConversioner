package com.challenge.paytmlabs.currencyconversion.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.challenge.paytmlabs.currencyconversion.Adapter.CurrencyGridAdapter;
import com.challenge.paytmlabs.currencyconversion.Database.Realm.CurrencyModel;
import com.challenge.paytmlabs.currencyconversion.R;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Main Activity of the Application
 * This class binds view by ids and initializes the spinner and recycler view grid.
 * It finally launches the main application.
 * @author Rafay Tanzzel
 */
public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, RealmChangeListener<RealmResults<CurrencyModel>> {

    public RecyclerView rv;
    public Spinner dropdown;
    public Realm realm;
    public EditText editAmount;
    public double editRate = 1.0;;
    public double dbCMRate = 1.0;;
    private int storedSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv_currency_grid);
        dropdown = (Spinner) findViewById(R.id.currency_dropdown);
        editAmount = (EditText) findViewById(R.id.input_amount);
        editAmount.setOnFocusChangeListener(this);


        storedSpinnerPosition = 0;
        if (savedInstanceState != null) {
            storedSpinnerPosition = savedInstanceState.getInt("DefaultSpinner", 0);
            editRate = savedInstanceState.getDouble("EditRate", 1.0);
            dbCMRate = savedInstanceState.getDouble("DBSpinnerRate", 1.0);
        }


        realm = Realm.getDefaultInstance();
        RealmResults<CurrencyModel> datasource = realm.where(CurrencyModel.class).findAllAsync();

        rv.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns()));
        rv.setAdapter(new CurrencyGridAdapter(datasource, true, true, editAmount, dropdown, editRate, dbCMRate));

        datasource.addChangeListener(this);
    }

    /**
     * Dynamically calculates the number of columns the recycler view should display based
     * on screen resolution and size
     * @return The function returns the number of columns best suited for grid layout
     */
    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 120);
        return noOfColumns;
    }

    /**
     * Save all the values that need to survive during configuration change
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("DefaultSpinner", dropdown.getSelectedItemPosition());

        if(!editAmount.getText().toString().isEmpty()){
            editRate = Double.parseDouble(editAmount.getText().toString());
        }

        String spinner_current = dropdown.getSelectedItem().toString();
        CurrencyModel cm = realm.where(CurrencyModel.class).equalTo("currency", spinner_current).findFirst();
        dbCMRate = cm.getRate();

        outState.putDouble("EditRate", editRate);
        outState.putDouble("DBSpinnerRate", dbCMRate);
        super.onSaveInstanceState(outState);
    }


    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            hideKeyboard(view);
        }
    }

    @Override
    public void onChange(RealmResults<CurrencyModel> currencyModels) {
        Object[] CM = currencyModels.toArray();
        Set<String> spinnerSet = new HashSet<String>();
        List<String> spinnerValues = new LinkedList<String>();
        spinnerSet.add("USD");
        spinnerValues.add("USD");
        for (Object element : CM) {
            CurrencyModel cm_item = (CurrencyModel) element;
            if(!spinnerSet.contains(cm_item.getCurrency())) {
                spinnerValues.add(cm_item.getCurrency());
                spinnerSet.add(cm_item.getCurrency());
            }
        }
        ArrayAdapter<String> spinner = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, spinnerValues);
        dropdown.setAdapter(spinner);
        dropdown.setSelection(storedSpinnerPosition);
    }
}
