package com.bsm.mobile.legacy.module.sminfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.SideMissionInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/25/2017.
 */

public class GoogleDriveLinkAdapter extends FirebaseRecyclerAdapter<SideMissionInfo, GoogleDriveLinkAdapter.SideMissionsInfoViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GoogleDriveLinkAdapter(@NonNull FirebaseRecyclerOptions<SideMissionInfo> options) {
        super(options);
    }

    @NonNull
    @Override
    public SideMissionsInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_google_drive_link, parent, false);

        return new SideMissionsInfoViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull SideMissionsInfoViewHolder holder, int position, @NonNull SideMissionInfo model) {
        holder.setSideMissionInfo(model);
    }


    public class SideMissionsInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.item_name_text_view)
        TextView sideMissionName;
        @BindView(R.id.item_parent)
        View sideMissionParent;

        private Intent googleDriveIntent;

        private Context context;

        public SideMissionsInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sideMissionParent.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            context.startActivity(googleDriveIntent);
        }

        void setSideMissionInfo( SideMissionInfo sminfo )
        {
            sideMissionName.setText(sminfo.getName());
            googleDriveIntent = new Intent(Intent.ACTION_VIEW);
            googleDriveIntent.setData(Uri.parse(sminfo.getLink()));
        }
    }
}
