package com.bsm.mobile.domain.wizard;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.bsm.mobile.common.resource.TeamResources.getColor;

public class WizardView extends LinearLayout {

    private final Context context;
    private final float scale;

    private final String userName;
    private final String userImageUrl;
    private final String team;

    public WizardView(Context context, String userName, String userImageUrl, String team) {
        super(context);
        this.context = context;
        scale = context.getResources().getDisplayMetrics().density;
        this.userName = userName;
        this.userImageUrl = userImageUrl;
        this.team = team;

        setView();
    }

    private void setView() {
        int PADDING_PX = dpToPx(8);
        setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        setPadding(PADDING_PX, PADDING_PX, PADDING_PX, PADDING_PX);
        setOrientation(HORIZONTAL);

        addView(getUserImageView());
        addView(getUserNameTextView());
    }

    private CircleImageView getUserImageView() {
        CircleImageView imageView = new CircleImageView(context);
        imageView.setLayoutParams(params(36));
        Glide.with(context).load(userImageUrl).into(imageView);

        return imageView;
    }

    private int dpToPx(int dp){
        return (int) (dp * scale + 0.5f);
    }

    private ViewGroup.LayoutParams params(int dp){
        return new LayoutParams(dpToPx(dp), dpToPx(dp));
    }

    private TextView getUserNameTextView() {
        TextView textView = new TextView(context);
        textView.setText(String.format("  %s", userName));
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTextColor(getColor(context, team));
        textView.setLayoutParams(params(36));

        return textView;
    }
}
