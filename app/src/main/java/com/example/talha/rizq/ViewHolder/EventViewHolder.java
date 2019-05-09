package com.example.talha.rizq.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.talha.rizq.Interfaces.ItemClickListener;
import com.example.talha.rizq.R;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView event_desc, event_loc, event_time;
    public ItemClickListener listener;
    public RelativeLayout container;
    public CardView card;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        event_desc = (TextView) itemView.findViewById(R.id.user_event_description);
        event_loc = (TextView) itemView.findViewById(R.id.user_event_location);
        event_time = (TextView) itemView.findViewById(R.id.user_event_time);
        container = (RelativeLayout)itemView.findViewById(R.id.relative_layout_events);
        card = (CardView)itemView.findViewById(R.id.event_cards);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
