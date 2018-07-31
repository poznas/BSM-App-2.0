package com.bsm.mobile.domain.wizard.badge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.domain.wizard.WizardView;
import com.bsm.mobile.domain.wizard.badge.model.BadgeInfoView;
import com.bsm.mobile.legacy.model.User;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class BadgeInfoAdapter extends RecyclerView.Adapter<BadgeInfoAdapter.BadgeInfoViewHolder>{

    private List<BadgeInfoView> badgeInfoList;

    public BadgeInfoAdapter() {
        this.badgeInfoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BadgeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_badge_info, viewGroup, false);

        return new BadgeInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeInfoViewHolder badgeInfoViewHolder, int position) {
        badgeInfoViewHolder.setBadgeInfo(badgeInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return badgeInfoList.size();
    }

    public void updateBadgeInfoList(List<BadgeInfoView> list){
        badgeInfoList.clear();
        badgeInfoList.addAll(list);
        notifyDataSetChanged();
    }

    public class BadgeInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sm_image_view)
        CircleImageView sideMissionImage;
        @BindView(R.id.item_sm_name_view)
        TextView sideMissionTextView;

        @BindView(R.id.total_amount_text_view)
        TextView totalAmountTextView;
        @BindView(R.id.remaining_badges_text_view)
        TextView remainingBadgesTextView;
        @BindView(R.id.required_executions_text_view)
        TextView requiredExecutionsTextView;

        @BindView(R.id.awarded_users_text)
        TextView awardedUsersHeader;
        @BindView(R.id.awarded_users_box)
        LinearLayout awardedUsersBox;

        Context context;

        public BadgeInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void setBadgeInfo(BadgeInfoView data) {
            Glide.with(context).load(data.getBadgeInfo().getSideMissionImageUrl()).into(sideMissionImage);
            sideMissionTextView.setText(data.getBadgeInfo().getSideMissionName());
            totalAmountTextView.setText(String.valueOf(data.getBadgeInfo().getTotalAmount()));
            remainingBadgesTextView.setText(String.valueOf(data.getBadgeInfo().getRemainingBadges()));
            requiredExecutionsTextView.setText(String.valueOf(data.getBadgeInfo().getRequiredExecutions()));

            awardedUsersHeader.setVisibility(data.getAwardedUsers().size() > 0 ? VISIBLE : GONE);
            awardedUsersBox.removeAllViews();
            for(User user : data.getAwardedUsers()){
                awardedUsersBox.addView(new WizardView(
                        context,
                        user.getDisplayName(),
                        user.getPhotoUrl(),
                        user.getTeam()));
            }
        }
    }
}
