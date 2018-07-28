package com.bsm.mobile.legacy.domain.calendar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.CalendarTime;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mlody Danon on 7/25/2017.
 */

public class CalendarTimesAdapter extends FirebaseRecyclerAdapter<CalendarTime, CalendarTimesAdapter.CalendarTimesViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CalendarTimesAdapter(@NonNull FirebaseRecyclerOptions<CalendarTime> options) {
        super(options);
    }

    @Override
    public CalendarTimesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_time, parent, false);

        return new CalendarTimesViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull CalendarTimesViewHolder holder, int position, @NonNull CalendarTime model) {
        holder.setCalendarTime(model);
    }


    public class CalendarTimesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_time_text_view)
        TextView itemTimeView;
        @BindView(R.id.item_info_text_view)
        TextView itemInfoView;

        public CalendarTimesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCalendarTime(CalendarTime calendarTime) {
            itemTimeView.setText(calendarTime.getTime());
            itemInfoView.setText(calendarTime.getInfo());
        }
    }
}
