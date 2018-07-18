package com.bsm.mobile.legacy.module.wizard.list;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.User;
import com.bumptech.glide.Glide;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mlody Danon on 7/24/2017.
 */

public class UsersAdapter extends FirebaseRecyclerAdapter<User, UsersAdapter.UserViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UsersAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wizzard, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        holder.setUser(model);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_user_image_view)
        CircleImageView itemUserImage;
        @BindView(R.id.item_display_name_text_view)
        TextView itemUserDisplayName;
        @BindView(R.id.item_team_text_view)
        TextView itemUserTeam;
        @BindView(R.id.item_user_parent)
        View itemUserParent;

        private Context context;

        private Intent facebookIntent;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemUserParent.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            context.startActivity(facebookIntent);
        }

        void setUser( User user ){
            itemUserDisplayName.setText(user.getDisplayName());
            itemUserTeam.setText(user.getTeam());

            switch (user.getTeam()){
                case "cormeum":
                    itemUserTeam.setTextColor(context.getResources().getColor(R.color.red));
                    break;
                case "sensum":
                    itemUserTeam.setTextColor(context.getResources().getColor(R.color.blue));
                    break;
                case "mutinium":
                    itemUserTeam.setTextColor(context.getResources().getColor(R.color.green));
                    break;
                default:
                    break;
            }

            Glide.with(context)
                    .load(user.getPhotoUrl())
                    .into(itemUserImage);

            facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(getFacebookPageURL(user)));
        }


        public String getFacebookPageURL(User user ){
           PackageManager packageManager = context.getPackageManager();
           try{
               int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0 ).versionCode;
               if( versionCode >= 3002850 ){
                   return "fb://facewebmodal/f?href=" + user.getFacebook();
               } else {
                   return "fb://page/" + user.getFacebook();
               }
           } catch (PackageManager.NameNotFoundException e) {
               return user.getFacebook();
           }
       }
    }
}
