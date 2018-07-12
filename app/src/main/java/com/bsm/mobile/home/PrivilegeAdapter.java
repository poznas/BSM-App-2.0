package com.bsm.mobile.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.DiffResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.model.Privilege;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.*;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.Constants.REPORTS_LOADING;
import static com.bsm.mobile.Constants.REPORTS_NO_PENDING;

public class PrivilegeAdapter extends RecyclerView.Adapter<PrivilegeAdapter.PrivilegeViewHolder>{

    private List<Privilege> privileges;
    private HashMap<String, Intent> privilegeIntentMap;

    public PrivilegeAdapter(List<Privilege> privileges, Context context) {
        this.privileges = privileges;
        privilegeIntentMap = HomeIntentFactory.getPrivilegeIntentMap(context);
    }

    @NonNull
    @Override
    public PrivilegeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_privilege, parent, false);

        return new PrivilegeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivilegeViewHolder holder, int position) {
        holder.setPrivilegeData(privileges.get(position));
    }

    @Override
    public int getItemCount() {
        return privileges.size();
    }

    public void updatePrivileges(List<Privilege> newPrivileges){
        PrivilegeDiffUtil diffUtil = new PrivilegeDiffUtil(privileges, newPrivileges);
        DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
        privileges.clear();
        privileges.addAll(newPrivileges);
        diffResult.dispatchUpdatesTo(this);
    }

    public class PrivilegeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        @BindView(R.id.privilegeIcon)
        ImageView privilegeIcon;
        @BindView(R.id.privilegeBrand)
        TextView privilegeBrand;
        @BindView(R.id.privilegeParent)
        View privilegeParent;

        @BindView(R.id.progress_bar_view)
        ProgressBar progressBar;
        @BindView(R.id.pending_reports_view)
        TextView pendingReportsView;

        private Privilege privilege;

        private final Context context;

        public PrivilegeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            privilegeParent.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            context.startActivity(privilegeIntentMap.get(privilege.getBrand()));
        }

        public void setPrivilegeData(Privilege current) {
            this.privilege = current;

            privilegeBrand.setText(privilege.getBrand());
            privilegeIcon.setImageResource(privilege.getIconId());

            if( privilege.isCheckIfContain() ){
                switch (privilege.getPendingReports()){
                    case REPORTS_LOADING:
                        progressBar.setVisibility(VISIBLE);
                        pendingReportsView.setVisibility(GONE);
                        break;
                    case REPORTS_NO_PENDING :
                        progressBar.setVisibility(GONE);
                        pendingReportsView.setVisibility(GONE);
                        break;
                    default:
                        progressBar.setVisibility(GONE);
                        pendingReportsView.setVisibility(VISIBLE);
                        pendingReportsView.setText(String.valueOf(current.getPendingReports()));
                        break;
                }
            }
        }
    }
}
