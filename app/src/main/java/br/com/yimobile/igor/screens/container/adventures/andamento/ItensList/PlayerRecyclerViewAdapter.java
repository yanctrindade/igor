package br.com.yimobile.igor.screens.container.adventures.andamento.ItensList;

/**
 * Created by renne on 20/10/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.yimobile.igor.R;

public class PlayerRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<String> playerArrayList;
    private Context context;


    public PlayerRecyclerViewAdapter(ArrayList<String> sessions, Context context) {
        this.playerArrayList = sessions;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.player_list_item, parent, false);

        PlayerViewHolder holder = new PlayerViewHolder(view, context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PlayerViewHolder playerHolder = (PlayerViewHolder) holder;

        String title  = playerArrayList.get(position);

       // playerHolder.titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return playerArrayList.size();
    }
}
