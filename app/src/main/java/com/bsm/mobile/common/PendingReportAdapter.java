package com.bsm.mobile.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.PendingReport;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bsm.mobile.common.resource.Constants.dateFormat;
import static com.bsm.mobile.common.resource.Constants.timeFormat;

public class PendingReportAdapter extends RecyclerView.Adapter<PendingReportAdapter.PendingReportViewHolder>{

    private List<PendingReport> reports;
    private final ReportIntentFactory reportIntentFactory;

    public PendingReportAdapter(ReportIntentFactory.Mapper mapper) {
        this.reportIntentFactory = new ReportIntentFactory(mapper);
        reports = new ArrayList<>();
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
        holder.setReportData(reports.get(position));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void updatePendingReports(List<PendingReport> newReports) {
        reports.clear();
        reports.addAll(newReports);
        notifyDataSetChanged();
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

        private Context context;
        private PendingReport report;

        public PendingReportViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            parentView.setOnClickListener(this);

        }

        public void setReportData(PendingReport report) {
            this.report = report;

            dateView.setText(dateFormat.format(new Date(report.getTimestamp())));
            timeView.setText(timeFormat.format(new Date(report.getTimestamp())));

            smNameView.setText(report.getSm_name());
            userNameView.setText(report.getPerforming_user());

            Glide.with(context)
                    .load(report.getUser_photoUrl())
                    .into(userImageView);
        }

        @Override
        public void onClick(View view) {
            Intent intent = reportIntentFactory.get(report);
            if(intent != null){
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }
    }
}
