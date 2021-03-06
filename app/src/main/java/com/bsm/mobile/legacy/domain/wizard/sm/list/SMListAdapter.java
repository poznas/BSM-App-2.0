package com.bsm.mobile.legacy.domain.wizard.sm.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bsm.mobile.R;
import com.bsm.mobile.common.resource.Message;
import com.bsm.mobile.legacy.domain.wizard.sm.AddSMActivity;
import com.bsm.mobile.legacy.domain.wizard.sm.post.AddSMPostActivity;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;

/**
 * Created by Mlody Danon on 7/29/2017.
 */

@Setter
public class SMListAdapter extends FirebaseRecyclerAdapter<SideMissionInfo, SMListAdapter.SMListViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String team;
    private boolean reportsEnabled;


    public SMListAdapter(@NonNull FirebaseRecyclerOptions<SideMissionInfo> options) {
        super(options);
    }

    @NonNull
    @Override
    public SMListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_name_and_arrow, parent, false);

        return new SMListViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull SMListViewHolder holder, int position, @NonNull SideMissionInfo model) {
        holder.setInfo(model);
    }

    public class SMListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.item_name_text_view)
        TextView sideMissionName;
        @BindView(R.id.item_parent)
        View sideMissionParent;

        private Intent addSMDetailsIntent;
        private Bundle addSMDetailsBundle;

        private Context context;

        public SMListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sideMissionParent.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            if(reportsEnabled){
                if( addSMDetailsIntent != null && team != null ){
                    addSMDetailsIntent.putExtra("team",team);
                    context.startActivity(addSMDetailsIntent);
                    ((Activity)context).finish();
                }
            }else {
                Toast.makeText(context, Message.ADMIN_REPORT_DISABLED, Toast.LENGTH_SHORT).show();
            }

        }

        public void setInfo(SideMissionInfo info) {

            sideMissionName.setText(info.getName());
            addSMDetailsBundle = new Bundle();
            addSMDetailsBundle.putString("sm_name",info.getName());
            if( info.isPost() ){
                addSMDetailsIntent = new Intent(context,AddSMPostActivity.class);
            }else{
                addSMDetailsIntent = new Intent(context,AddSMActivity.class);
            }
            addSMDetailsIntent.putExtras(addSMDetailsBundle);
        }
    }
}
