package br.com.yimobile.igor.screens.container.adventures.andamento.ItensList;

/**
 * Created by renne on 03/10/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;

public class ItensRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<String> sessionsArrayList;
    private Context context;


    public ItensRecyclerViewAdapter(ArrayList<String> sessions, Context context) {
        this.sessionsArrayList = sessions;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.resume_list_item, parent, false);

        ItensViewHolder holder = new ItensViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItensViewHolder sessionsHolder = (ItensViewHolder) holder;

        String title  = sessionsArrayList.get(position);

        sessionsHolder.titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return sessionsArrayList.size();
    }

    public void swap() {
        notifyDataSetChanged();
    }
}
