package br.com.yimobile.igor.screens.container;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.yimobile.igor.R;
import database.Notifications;

import static br.com.yimobile.igor.screens.container.ContainerActivity.countUnread;
import static br.com.yimobile.igor.screens.auth.LoginActivity.dateToString;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notifications> arrayNotification;
    private Activity activity;

    public NotificationAdapter(List<Notifications> arrayNotification, Activity activity){
        this.arrayNotification = arrayNotification;
        this.activity = activity;
    }

    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder, int position) {
        final Notifications notification = arrayNotification.get(position);
        final NotificationAdapter.NotificationViewHolder h = holder;

        //Título
        holder.vTitleNotification.setText(notification.getAventuraNome());

        //Texto
        holder.vNotification.setText("Sessão marcada para: " + notification.getDateAgenda());

        //Data
        holder.vDataNotification.setText(notification.getDateEnvio());

        //Lida
        if(notification.getDateRecebimento() == null)
            holder.vLida.setVisibility(View.VISIBLE);
        else
            holder.vLida.setVisibility(View.INVISIBLE);

        //Clique
        holder.vClickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.notification_dialog);
                //dialog.setTitle("Title...");

                ImageView image = dialog.findViewById(R.id.image);

                // set the custom dialog components - text, image and button
                TextView text = dialog.findViewById(R.id.text);
                text.setText(notification.getAventuraNome());
                image.setImageResource(R.drawable.ic_warning_white_24dp);

                TextView conteudo = dialog.findViewById(R.id.conteudo) ;
                conteudo.setText("Sessão marcada para: " + notification.getDateAgenda());

                TextView date = dialog.findViewById(R.id.data);
                date.setText(notification.getDateEnvio());

                Button dialogButton = dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                final Calendar data_atual = Calendar.getInstance();
                notification.setDateRecebimento(dateToString(data_atual, "dd/MM/yyyy"));
                //UPDATE!!!
                h.vLida.setVisibility(View.INVISIBLE);
                ContainerActivity.menuItem.setIcon(buildCounterDrawable(countUnread(), R.drawable.ic_notifications_none_white_48dp));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrayNotification != null) return arrayNotification.size();
        return 0;
    }

    public void clear() {

        arrayNotification.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Notifications> list) {
        if(arrayNotification == null) arrayNotification = new ArrayList<>();
        if(list != null) arrayNotification.addAll(list);
        notifyDataSetChanged();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView vNotification, vDataNotification, vTitleNotification;
        ImageView vLida;
        RelativeLayout vClickArea;

        public NotificationViewHolder(View v){
            super(v);
            vNotification = v.findViewById(R.id.txtNotification);
            vDataNotification = v.findViewById(R.id.dataNotification);
            vTitleNotification = v.findViewById(R.id.titleNotification);
            vLida = v.findViewById(R.id.newNotification);
            vClickArea = v.findViewById(R.id.notification_click_area);
            //vNoNotification = v.findViewById(R.id.noNotification);
        }
   }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.unread_background, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(activity.getResources(), bitmap);
    }

}
