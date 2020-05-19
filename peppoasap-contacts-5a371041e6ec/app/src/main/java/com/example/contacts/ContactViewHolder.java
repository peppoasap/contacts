package com.example.contacts;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactViewHolder extends RecyclerView.ViewHolder{

    protected TextView name;
    protected TextView surname;
    protected ImageView call;
    protected ImageView sendEmail;
    protected CardView card;
    protected ConstraintLayout layout;

    public ContactViewHolder(@NonNull final View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.card_name);
        this.surname = itemView.findViewById(R.id.card_surname);
        this.call = itemView.findViewById(R.id.card_phone);
        this.sendEmail = itemView.findViewById(R.id.card_email);
        this.card = itemView.findViewById(R.id.card);
        this.layout = itemView.findViewById(R.id.layout_card);
    }

}
