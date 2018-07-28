package com.bsm.mobile.legacy.domain.judge.rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.sidemission.PropertyDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mlody Danon on 8/8/2017.
 */

public class RatePropertiesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<PropertyDetails> propertiesDetails;

    public RatePropertiesAdapter(Context context, List<PropertyDetails> propertiesDetails) {
        this.context = context;
        this.propertiesDetails = propertiesDetails;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {return propertiesDetails.size();}

    @Override
    public Object getItem(int position) {
        return propertiesDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_rate_sm_properity, parent, false );

        TextView propertyName = convertView.findViewById(R.id.item_properity_name);
        TextView propertyHint = convertView.findViewById(R.id.item_properity_hint);
        EditText propertyEditText = convertView.findViewById(R.id.item_properity_edit_text);
        Spinner propertySpinner = convertView.findViewById(R.id.item_properity_spinner);

        PropertyDetails current = propertiesDetails.get(position);

        propertyName.setText(current.getName());
        propertyHint.setText(current.getHint());

        if( current.getType().equals("normal_value") ){
            propertyEditText.setVisibility(View.VISIBLE);
            propertySpinner.setVisibility(View.GONE);
        }else{
            InitializeSpinner(current, position, propertySpinner);
            propertySpinner.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void InitializeSpinner( PropertyDetails current, int position, Spinner propertySpinner ) {

        switch (current.getType()){
            case "boolean_value":
                InitializeBooleanSpinner(current, position, propertySpinner);
                break;
            case "limited_value":
                InitializeLimitedSpinner(current, position, propertySpinner);
                break;
            case "spinner":
                InitializeClassicSpinner(current, position, propertySpinner);
            default:
                break;
        }
    }

    private void InitializeClassicSpinner(PropertyDetails current, int position, Spinner propertySpinner) {
        List<String> keys = new ArrayList<>();
        keys.add("<none>");
        keys.addAll(current.getSpinnerKeys());
        setPropertySpinner( propertySpinner, keys);
    }

    private void InitializeLimitedSpinner(PropertyDetails current, int position, Spinner propertySpinner) {

        List<String> values = new ArrayList<>();
        values.add("<none>");
        for( int i=1; i<=current.getLimitedValue(); i++ ){
            values.add(String.valueOf(i));
        }
        setPropertySpinner( propertySpinner, values);
    }

    private void InitializeBooleanSpinner( PropertyDetails current, int position, Spinner propertySpinner) {

        List<String> answers = new ArrayList<>();
        answers.add("<none>"); answers.add("TAK"); answers.add("NIE");
        setPropertySpinner( propertySpinner, answers);
    }

    private void setPropertySpinner(Spinner propertySpinner, List<String> values ){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertySpinner.setAdapter(dataAdapter);
    }
}
