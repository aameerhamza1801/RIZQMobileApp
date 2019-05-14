package com.example.talha.rizq.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.talha.rizq.Interfaces.ItemClickListener;
import com.example.talha.rizq.R;

public class AdminEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView event_desc, event_loc, event_time;
    public ItemClickListener listener;
    public CardView card;

    public AdminEventsViewHolder(@NonNull View itemView) {
        super(itemView);

        event_desc = (TextView) itemView.findViewById(R.id.admin_event_description);
        event_loc = (TextView) itemView.findViewById(R.id.admin_event_location);
        event_time = (TextView) itemView.findViewById(R.id.admin_event_time);
        card = (CardView)itemView.findViewById(R.id.event_cards_admin);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
