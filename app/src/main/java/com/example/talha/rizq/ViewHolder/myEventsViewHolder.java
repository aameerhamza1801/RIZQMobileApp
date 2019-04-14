package com.example.talha.rizq.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.talha.rizq.Interfaces.ItemClickListener;
import com.example.talha.rizq.R;

public class myEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView myevent_descr,myevent_loca,myevent_tim;
    private ItemClickListener itemClickListener;

    public myEventsViewHolder(@NonNull View itemView) {
        super(itemView);

        myevent_descr = itemView.findViewById(R.id.myevent_desc);
        myevent_loca = itemView.findViewById(R.id.myevent_loc);
        myevent_tim = itemView.findViewById(R.id.myevent_time);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
