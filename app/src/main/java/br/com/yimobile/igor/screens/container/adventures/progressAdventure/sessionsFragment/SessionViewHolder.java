package br.com.yimobile.igor.screens.container.adventures.progressAdventure.sessionsFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.yimobile.igor.R;

public class SessionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Context context;
    TextView titleTextView;

    public SessionViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        titleTextView = itemView.findViewById(R.id.sessao);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
    }
}
