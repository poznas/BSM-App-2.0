package com.bsm.mobile.legacy.module.judge.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PendingReport;
import com.bsm.mobile.legacy.module.judge.rate.JudgeRateSMPostActivity;
import com.bsm.mobile.legacy.module.judge.rate.JudgeRateSMActivity;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mlody Danon on 8/1/2017.
 */

public class PendingReportAdapter extends RecyclerView.Adapter<PendingReportAdapter.PendingReportViewHolder> implements Tagable{

    private List<PendingReport> pendingReports;

    public PendingReportAdapter() {
        pendingReports = new ArrayList<>();
    }

    @NonNull
    @Override
    public PendingReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_report, parent, false);

        return new PendingReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingReportViewHolder holder, int position) {
        holder.setReportData(pendingReports.get(position));
    }

    public void updatePendingReports(List<PendingReport> newPendingReports){
        pendingReports.clear();
        pendingReports.addAll(newPendingReports);
        notifyDataSetChanged();
        Log.d(getTag(), "pending reports updated : " + pendingReports);
    }

    @Override
    public int getItemCount() {
        return pendingReports.size();
    }

    public class PendingReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.item_parent)
        View parentView;
        @BindView(R.id.item_sm_name_view)
        TextView smNameView;
        @BindView(R.id.item_user_image_view)
        CircleImageView userImageView;
        @BindView(R.id.item_user_name_view)
        TextView userNameView;
        @BindView(R.id.item_time_text_view)
        TextView timeView;
        @BindView(R.id.item_date_text_view)
        TextView dateView;

        private Intent JudgeIntent;
        private Bundle JudgeBundle;
        private Context context;

        public PendingReportViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView.setOnClickListener(this);
            context = itemView.getContext();
        }

        public void setReportData(PendingReport report) {

            Date dateObject = new Date(report.getTimestamp());
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            String date = sdfDate.format(dateObject);
            String time = sdfTime.format(dateObject);

            dateView.setText(date);
            timeView.setText(time);

            smNameView.setText(report.getSm_name());
            userNameView.setText(report.getPerforming_user());

            Glide.with(context)
                    .load(report.getUser_photoUrl())
                    .into(userImageView);

            JudgeBundle = new Bundle();
            JudgeBundle.putString("sm_name",report.getSm_name());
            JudgeBundle.putString("user_name",report.getPerforming_user());
            JudgeBundle.putString("user_image_url",report.getUser_photoUrl());
            JudgeBundle.putString("time",time);
            JudgeBundle.putString("date",date);
            JudgeBundle.putString("rpid",report.getRpid());
            if(!report.isPost()){
                JudgeIntent = new Intent(context, JudgeRateSMActivity.class);
                JudgeIntent.putExtras(JudgeBundle);
            }else{
                JudgeIntent = new Intent(context, JudgeRateSMPostActivity.class);
                JudgeIntent.putExtras(JudgeBundle);
            }

        }

        @Override
        public void onClick(View v) {
            if( JudgeIntent != null ){
                context.startActivity(JudgeIntent);
                ((Activity)context).finish();
            }
        }
    }
}
