package com.bsm.mobile.points.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.TeamResources;
import com.bsm.mobile.common.SimpleAlertDialog;
import com.bsm.mobile.common.SimpleDialogClickListener;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.bsm.mobile.points.PointsIntentFactory;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import lombok.Setter;

import static android.graphics.Typeface.*;
import static android.view.View.GONE;
import static com.bsm.mobile.Constants.*;
import static com.bsm.mobile.Message.*;
import static com.bsm.mobile.Message.MESSAGE_DIALOG_INVALIDATE;
import static com.bsm.mobile.points.list.PointsListActivityMVP.*;

@Setter
public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.PointsViewHolder> implements Tagable{

    private List<PointsInfo> pointsRecords;

    private Presenter presenter;
    private boolean invalidationPermission;

    public PointsAdapter(Presenter presenter) {
        pointsRecords = new ArrayList<>();
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public PointsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_points, viewGroup, false);

        return new PointsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PointsViewHolder holder, int position) {
        holder.setPointsInfo(pointsRecords.get(position));
    }

    @Override
    public int getItemCount() {
        return pointsRecords.size();
    }

    public void updatePointsRecords(List<PointsInfo> records) {
        pointsRecords.clear();
        pointsRecords.addAll(records);
        notifyDataSetChanged();
    }

    @SuppressLint("SimpleDateFormat")
    class PointsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_parent)
        View itemParent;
        @BindView(R.id.item_time_text_view)
        TextView itemTime;
        @BindView(R.id.item_date_text_view)
        TextView itemDate;
        @BindView(R.id.item_user_image_view)
        CircleImageView itemUserImage;
        @BindView(R.id.item_description_text_view)
        TextView itemDescription;
        @BindView(R.id.item_label_text_view)
        TextView itemLabel;
        @BindView(R.id.item_points_text_view)
        TextView itemPoints;

        private final SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd MMMM");
        private final SimpleDateFormat timeFormat =
                new SimpleDateFormat("HH:mm");

        Context context;

        private PointsInfo pointsInfo;


        public PointsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            setClickListener();
            setInvalidateClickListener();
        }

        private void setClickListener() {
            itemParent.setOnClickListener(event -> {
                Intent intent = PointsIntentFactory.getDisplayDetailsIntent(context, pointsInfo);
                if(intent != null){
                    Log.d(getTag(), "on Click : " + pointsInfo);
                    context.startActivity(intent);
                }
            });
        }

        private void setInvalidateClickListener() {

            itemParent.setOnLongClickListener(event -> {
                if(invalidationPermission){
                    new SimpleAlertDialog(context)
                            .setMessage(MESSAGE_DIALOG_INVALIDATE)
                            .setOnPositiveClick(() -> presenter.invalidatePoints(pointsInfo))
                            .show();
                    return true;
                }
                return false;
            });
        }

        void setPointsInfo(PointsInfo pointsInfo) {
            this.pointsInfo = pointsInfo;

            itemDate.setText(dateFormat.format(new Date(pointsInfo.getTimestamp())));
            itemTime.setText(timeFormat.format(new Date(pointsInfo.getTimestamp())));

            itemPoints.setText(String.valueOf(pointsInfo.getPoints()));
            itemPoints.setTextColor(TeamResources.COLORS(context).get(pointsInfo.getTeam()));

            itemLabel.setText(pointsInfo.getLabel());

            switch (pointsInfo.getLabel()){
                case LABEL_POINTS_BET:
                case LABEL_POINTS_MEDAL:
                    itemUserImage.setVisibility(GONE);
                    itemDescription.setText(pointsInfo.getInfo());
                    itemDescription.setTypeface(null, NORMAL);
                    break;
                case LABEL_POINTS_MAIN_COMPETITION:
                    itemUserImage.setVisibility(GONE);
                    itemDescription.setText(pointsInfo.getName());
                    itemDescription.setTypeface(null, BOLD);
                    break;
                default:
                    itemUserImage.setVisibility(View.VISIBLE);
                    itemDescription.setText(pointsInfo.getUser_name());
                    itemDescription.setTypeface(null, BOLD);

                    Glide.with(context)
                            .load(pointsInfo.getUser_photo())
                            .into(itemUserImage);
                    break;
            }
        }
    }
}
