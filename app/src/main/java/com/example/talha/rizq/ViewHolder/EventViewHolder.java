package com.example.talha.rizq.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.talha.rizq.Interfaces.ItemClickListener;
import com.example.talha.rizq.R;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView event_desc, event_loc, event_time;
    public Button event_volunteer;
    public ItemClickListener listener;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        event_desc = (TextView) itemView.findViewById(R.id.user_event_description);
        event_loc = (TextView) itemView.findViewById(R.id.user_event_location);
        event_time = (TextView) itemView.findViewById(R.id.user_event_time);
        event_volunteer = (Button) itemView.findViewById(R.id.user_event_volunteer);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
