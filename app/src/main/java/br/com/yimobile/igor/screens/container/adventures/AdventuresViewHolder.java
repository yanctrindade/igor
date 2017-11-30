package br.com.yimobile.igor.screens.container.adventures;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;

public class AdventuresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    Adventure adventure;
    TextView titleTextView;
    TextView subtitleTextView;

    AdventuresViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        titleTextView = itemView.findViewById(R.id.adventures_title);
        subtitleTextView = itemView.findViewById(R.id.adventures_subtitle);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        ((ContainerActivity) context).onAdventureItemClicked(getAdapterPosition(), adventure);
    }
}
