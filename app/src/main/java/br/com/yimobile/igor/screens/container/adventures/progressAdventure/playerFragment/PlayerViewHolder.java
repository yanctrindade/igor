package br.com.yimobile.igor.screens.container.adventures.progressAdventure.playerFragment;

/**
 * Created by renne on 20/10/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Context context;

    public PlayerViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
    }
}
