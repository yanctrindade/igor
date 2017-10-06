package br.com.yimobile.igor.screens.container.adventures.andamento.ItensList;

/**
 * Created by renne on 03/10/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;

public class ItensViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Context context;
    TextView titleTextView;

    public ItensViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        titleTextView = (TextView) itemView.findViewById(R.id.sessao);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
    }
}
