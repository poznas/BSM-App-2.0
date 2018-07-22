package com.bsm.mobile.legacy.module.points.details.sm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.sidemission.PropertyToDisplay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;

/**
 * Created by Mlody Danon on 7/29/2017.
 */

@AllArgsConstructor
public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.PropertiesViewHolder>{

    private List<PropertyToDisplay> mPropertiesList;

    @NonNull
    @Override
    public PropertiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_properities_component, parent, false );


        return new PropertiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertiesViewHolder holder, int position) {
        holder.setPropertyData(mPropertiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPropertiesList.size();
    }

    class PropertiesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_properity_name_view)
        TextView nameView;
        @BindView(R.id.item_properity_grade_view)
        TextView gradeView;


        PropertiesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setPropertyData(PropertyToDisplay current) {

            nameView.setText(current.getName());
            Double rounded = (double) Math.round(current.getGrade()*100)/100;
            gradeView.setText(String.valueOf(rounded));
        }
    }
}
