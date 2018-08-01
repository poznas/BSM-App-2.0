package com.bsm.mobile.domain.points.ranking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.PointsInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bsm.mobile.common.resource.TeamResources.getColor;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private List<PointsInfo> rankingList;

    public RankingAdapter() {
        this.rankingList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_wizard_ranking, viewGroup, false);

        return new RankingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder rankingViewHolder, int position) {
        rankingViewHolder.setLayout(rankingList.get(position), position);

    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public void updateRanking(List<PointsInfo> newList){
        rankingList.clear();
        rankingList.addAll(newList);
        notifyDataSetChanged();
    }

    public class RankingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ranking_number)
        TextView rankingNumberView;
        @BindView(R.id.item_user_image_view)
        CircleImageView userImageView;
        @BindView(R.id.item_display_name_text_view)
        TextView userNameView;
        @BindView(R.id.item_points_text_view)
        TextView pointsView;

        Context context;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void setLayout(PointsInfo pointsInfo, int position) {
            rankingNumberView.setText(String.valueOf(position + 1));

            Glide.with(context)
                    .load(pointsInfo.getUser_photo())
                    .into(userImageView);
            pointsView.setText(String.valueOf(pointsInfo.getPoints()));
            pointsView.setTextColor(getColor(context, pointsInfo.getTeam()));
            userNameView.setText(pointsInfo.getUser_name());
        }
    }
}
