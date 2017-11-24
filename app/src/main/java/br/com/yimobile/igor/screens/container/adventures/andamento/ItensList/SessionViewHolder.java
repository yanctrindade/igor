package br.com.yimobile.igor.screens.container.adventures.andamento.ItensList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;

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
