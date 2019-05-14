package com.example.talha.rizq.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talha.rizq.Interfaces.ItemClickListener;
import com.example.talha.rizq.R;

public class AdminCasesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView case_image;
    public TextView case_name, case_desc, case_account, case_amount;
    private ItemClickListener itemClickListener;
    public CardView card;

    public AdminCasesViewHolder(@NonNull View itemView) {
        super(itemView);

        case_image = (ImageView)itemView.findViewById(R.id.admin_case_image);
        case_name = (TextView) itemView.findViewById(R.id.admin_case_name);
        case_desc = (TextView)itemView.findViewById(R.id.admin_case_description);
        case_account = (TextView)itemView.findViewById(R.id.admin_case_account);
        case_amount = (TextView)itemView.findViewById(R.id.admin_case_amount);
        card = (CardView)itemView.findViewById(R.id.support_cards_admin);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
