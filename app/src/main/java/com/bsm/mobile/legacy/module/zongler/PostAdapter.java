package com.bsm.mobile.legacy.module.zongler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.ZonglerPost;
import com.bsm.mobile.legacy.module.PhotoVideoFullscreenDisplay;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.*;

/**
 * Created by Mlody Danon on 7/28/2017.
 */

public class PostAdapter extends FirebaseRecyclerAdapter<ZonglerPost, PostAdapter.PostDaysViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PostAdapter(@NonNull FirebaseRecyclerOptions<ZonglerPost> options) {
        super(options);
    }

    @NonNull
    @Override
    public PostDaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_zongler_post, parent, false);

        return new PostDaysViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostDaysViewHolder holder, int position, @NonNull ZonglerPost model) {
        holder.setPost(model);
    }

    public class PostDaysViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        @BindView(R.id.item_user_name_view)
        TextView userNameView;
        @BindView(R.id.item_user_image_view)
        CircleImageView userImageView;
        @BindView(R.id.item_title_view)
        TextView titleView;
        @BindView(R.id.thumbnail_parent_view)
        View thumbnailParentView;
        @BindView(R.id.thumbnail_view)
        ImageView thumbnailView;
        @BindView(R.id.play_button_view)
        ImageView playButtonView;
        @BindView(R.id.item_body_view)
        TextView bodyView;
        @BindView(R.id.item_time_view)
        TextView timeView;
        @BindView(R.id.item_date_view)
        TextView dateView;

        private Intent fullscreenIntent;
        private Bundle fullscreenBundle;

        private Context context;

        public PostDaysViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            thumbnailParentView.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            if( fullscreenIntent != null ){
                context.startActivity(fullscreenIntent);
            }
        }

        public void setPost(ZonglerPost post) {

            Date dateObject = new Date(post.getTimestamp());
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            String date = sdfDate.format(dateObject);
            String time = sdfTime.format(dateObject);

            dateView.setText(date);
            timeView.setText(time);
            userNameView.setText(post.getAuthor());
            titleView.setText(post.getTitle());

            String body = post.getBody().replace("\n","\r\n");
            bodyView.setText(body);

            Glide.with(context)
                    .load(post.getAuthorPhotoUrl())
                    .into(userImageView);

            String thumbnailUrl = post.getThumbnailUrl();

            if( thumbnailUrl != null ) {

                thumbnailParentView.setVisibility(VISIBLE);

                Glide.with(context)
                        .load(thumbnailUrl)
                        .into(thumbnailView);

                playButtonView.setVisibility((post.getVideoUrl() != null) ? VISIBLE : INVISIBLE);

                setThumbnailClickListener(post);
            }else {
                thumbnailParentView.setVisibility(GONE);
            }
        }

        private void setThumbnailClickListener(ZonglerPost post){

            fullscreenIntent = new Intent(context, PhotoVideoFullscreenDisplay.class);
            fullscreenBundle = new Bundle();

            String ImageUrl = post.getImageUrl();
            String videoUrl = post.getVideoUrl();
            if( ImageUrl != null ){
                fullscreenBundle.putString("type","photo");
                fullscreenBundle.putString("URL",ImageUrl);
            }else{
                fullscreenBundle.putString("type","video");
                fullscreenBundle.putString("URL",videoUrl);
            }
            fullscreenIntent.putExtras(fullscreenBundle);
        }
    }
}
