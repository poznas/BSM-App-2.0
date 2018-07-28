package com.bsm.mobile.professor.admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsm.mobile.Message;
import com.bsm.mobile.R;
import com.bsm.mobile.TeamResources;
import com.bsm.mobile.common.NullFighter;
import com.bsm.mobile.common.SimpleAlertDialog;
import com.bsm.mobile.common.Tagable;
import com.bsm.mobile.common.utils.UserDataUtils;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.legacy.module.calendar.CalendarDaysActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Color.TRANSPARENT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bsm.mobile.Constants.GENDER_FEMALE;
import static com.bsm.mobile.Constants.GENDER_MALE;
import static com.bsm.mobile.professor.admin.AdminActivityMVP.Presenter;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Tagable, NullFighter{

    private List<User> users;
    private Presenter presenter;

    public UserAdapter(Presenter presenter) {
        users = new ArrayList<>();
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_user, viewGroup, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        userViewHolder.setUser(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<User> newUsers){
        users.clear();
        users.addAll(newUsers);
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_user_parent)
        View parentView;
        @BindView(R.id.item_user_image_view)
        CircleImageView userImageView;
        @BindView(R.id.item_display_name_text_view)
        TextView displayNameView;
        @BindView(R.id.item_team_text_view)
        TextView teamNameView;
        @BindView(R.id.label_text_view)
        TextView labelView;
        @BindView(R.id.at_icon)
        ImageView atIconImageView;
        @BindView(R.id.male_icon)
        ImageView maleIconImageView;
        @BindView(R.id.female_icon)
        ImageView femaleIconImageView;
        @BindView(R.id.fb_icon)
        ImageView facebookIconImageView;

        Context context;
        private User user;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            setOnEditClickListener();
            setOnDeleteClickListener();
        }

        private void setOnDeleteClickListener() {
            parentView.setOnLongClickListener(event -> {
                new SimpleAlertDialog(context)
                        .setMessage(Message.ADMIN_DELETE_USER_DIALOG)
                        .setOnPositiveClick(() -> presenter.deleteUser(user))
                        .show();
                return true;
            });
        }

        private void setOnEditClickListener() {
            parentView.setOnClickListener(event -> {
                context.startActivity(new Intent(context, CalendarDaysActivity.class)); //TODO:
            });
        }

        public void setUser(User user) {
            this.user = user;

            Glide.with(context).load(user.getPhotoUrl()).into(userImageView);
            displayNameView.setText(user.getDisplayName());
            teamNameView.setText(user.getTeam());
            teamNameView.setTextColor(UserDataUtils.validTeam(user) ?
                    TeamResources.COLORS(context).get(user.getTeam()) : TRANSPARENT);

            labelView.setText(user.getLabel());

            atIconImageView.setVisibility(isNull(user.getEmail()) ? GONE : VISIBLE);
            maleIconImageView.setVisibility(GENDER_MALE.equals(user.getGender()) ? VISIBLE : GONE);
            femaleIconImageView.setVisibility(GENDER_FEMALE.equals(user.getGender()) ? VISIBLE : GONE);
            facebookIconImageView.setVisibility(isNull(user.getFacebook()) ? GONE : VISIBLE);
        }
    }
}
