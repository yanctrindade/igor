package br.com.yimobile.igor.screens.container.adventures;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import br.com.yimobile.igor.R;
import database.Adventure;
import database.Session;

public class AdventuresRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Adventure> adventuresArrayList;
    private Context context;

    AdventuresRecyclerViewAdapter(ArrayList<Adventure> adventures, Context context) {
        this.adventuresArrayList = adventures;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adventures_list_item, parent, false);

        return new AdventuresViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AdventuresViewHolder adventureHolder = (AdventuresViewHolder) holder;
        ArrayList<Session> sessoes = (ArrayList<Session>) adventuresArrayList.get(position).getSessoes();

        String title  = adventuresArrayList.get(position).getNome();
        String descr  = adventuresArrayList.get(position).getDescricao();
        adventureHolder.adventure = adventuresArrayList.get(position);
        adventureHolder.titleTextView.setText(title);
        adventureHolder.subtitleTextView.setText(descr);

        if(sessoes != null && adventuresArrayList.get(position).getData() != null && sessoes.size() > 0) {
            Collections.sort(sessoes, new Comparator<Session>() {
                public int compare(Session s1, Session s2) {
                    Date d1 = null, d2 = null;
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        d1 = formatter.parse(s1.getData());
                        d2 = formatter.parse(s2.getData());
                    } catch (java.text.ParseException ie) {
                        ie.printStackTrace();
                    }
                    return d1.compareTo(d2);
                }
            });

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date currentTime = Calendar.getInstance().getTime();
            Date d1 = null, d2 = null;

            try {
                d1 = formatter.parse(adventuresArrayList.get(position).getData());
                d2 = formatter.parse(sessoes.get(0).getData());

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            long diferencaInicioFim = d2.getTime() - d1.getTime();
            long diferencaHojeInicio = currentTime.getTime() - d1.getTime();

            float porcentagem;
            if(diferencaInicioFim > 0 && diferencaHojeInicio < diferencaInicioFim) {
                porcentagem = (((float)diferencaHojeInicio/(float)diferencaInicioFim));
            }else{
                porcentagem = 1;
            }

            adventureHolder.progresso.setMax(100);
            adventureHolder.progresso.setProgress((int)(porcentagem * 100));
        }
        switch (adventuresArrayList.get(position).getBackground()){
            case 0:
                adventureHolder.backgroundImageView.setImageResource(R.drawable.adv_bg_1);
                break;
            case 1:
                adventureHolder.backgroundImageView.setImageResource(R.drawable.adv_bg_2);
                break;
            case 2:
                adventureHolder.backgroundImageView.setImageResource(R.drawable.adv_bg_3);
                break;
            case 3:
                adventureHolder.backgroundImageView.setImageResource(R.drawable.adv_bg_4);
                break;
            case 4:
                adventureHolder.backgroundImageView.setImageResource(R.drawable.adv_bg_5);
                break;
            default:
                adventureHolder.backgroundImageView.setImageResource(R.drawable.adv_bg_1);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return adventuresArrayList.size();
    }

    void swap() {
        notifyDataSetChanged();
    }
}
