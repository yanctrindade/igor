package br.com.yimobile.igor;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AdventuresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Context context;
    TextView titleTextView;
    TextView subtitleTextView;

    public AdventuresViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        titleTextView = (TextView) itemView.findViewById(R.id.adventures_title);
        subtitleTextView = (TextView) itemView.findViewById(R.id.adventures_subtitle);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}
