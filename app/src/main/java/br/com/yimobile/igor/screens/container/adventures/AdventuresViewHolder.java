package br.com.yimobile.igor.screens.container.adventures;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;

public class AdventuresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    Adventure adventure;
    TextView titleTextView;
    TextView subtitleTextView;
    ImageView backgroundImageView;
    SeekBar progresso;

    AdventuresViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        titleTextView = itemView.findViewById(R.id.adventures_title);
        subtitleTextView = itemView.findViewById(R.id.adventures_subtitle);
        backgroundImageView = itemView.findViewById(R.id.background_img);
        progresso = itemView.findViewById(R.id.adv_seekbar);
        progresso.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        ((ContainerActivity) context).onAdventureItemClicked(getAdapterPosition(), adventure);
    }
}
