package br.com.yimobile.igor.screens.container.adventures.progressAdventure.sessionsFragment;

import android.app.Dialog;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.auth.LoginActivity;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.Adventure;
import database.Session;

public class SessionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Activity activity;
    Session session;
    Adventure adventure;
    TextView titleTextView;

    SessionViewHolder(View itemView, Activity activity) {
        super(itemView);

        this.activity = activity;
        titleTextView = itemView.findViewById(R.id.sessao);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.session_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView text = dialog.findViewById(R.id.text);
        text.setText(String.format("%s - %s", session.getTitulo(), session.getData()));

        String uid = ((ContainerActivity) activity).getUid();
        int estado = 1; // 0 - confirmado, 1 - convidado, 2 - cancelado
        StringBuilder jogadoresConfirmados = new StringBuilder();
        if(session.getJogadoresConfirmados() != null) {
            for (int i = 0; i < session.getJogadoresConfirmados().size(); i++) {
                String idJogador = session.getJogadoresConfirmados().get(i);
                String name = getUsername(idJogador);
                if (idJogador.equals(uid)) estado = 0;
                if (!name.isEmpty()) jogadoresConfirmados.append(name).append("<br/>");
            }
        }
        StringBuilder jogadoresConvidados = new StringBuilder();
        if(session.getJogadoresConvidados() != null) {
            for (int i = 0; i < session.getJogadoresConvidados().size(); i++) {
                String idJogador = session.getJogadoresConvidados().get(i);
                String name = getUsername(idJogador);
                if (idJogador.equals(uid)) estado = 1;
                if (!name.isEmpty()) jogadoresConvidados.append(name).append("<br/>");
            }
        }
        StringBuilder jogadoresCancelados = new StringBuilder();
        if(session.getJogadoresCancelados() != null) {
            for (int i = 0; i < session.getJogadoresCancelados().size(); i++) {
                String idJogador = session.getJogadoresCancelados().get(i);
                String name = getUsername(idJogador);
                if (idJogador.equals(uid)) estado = 2;
                if (!name.isEmpty()) jogadoresCancelados.append(name).append("<br/>");
            }
        }

        String sourceString = "";
        if(!jogadoresConfirmados.toString().isEmpty()) {
            sourceString += "<b>" + "Jogadores confirmados:" + "</b><br/>" + jogadoresConfirmados.toString();
        }
        if(!jogadoresConvidados.toString().isEmpty()){
            sourceString += "<br/><b>" + "Jogadores convidados:" + "</b><br/>" + jogadoresConvidados.toString();
        }
        if(!jogadoresCancelados.toString().isEmpty()){
            sourceString += "<br/><b>" + "Jogadores cancelados:" + "</b><br/>" + jogadoresCancelados.toString();
        }

        TextView conteudo = dialog.findViewById(R.id.conteudo) ;
        conteudo.setText(Html.fromHtml(sourceString));

        Button dialogButtonOK = dialog.findViewById(R.id.dialogButtonOK);
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final int finalEstado = estado;

        Button dialogButtonConfirmar = dialog.findViewById(R.id.dialogButtonConfirmar);
        dialogButtonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ContainerActivity) activity).setSessionConfirmed(adventure, session,
                        getAdapterPosition(), (finalEstado == 2));
                dialog.dismiss();
            }
        });

        Button dialogButtonCancelar = dialog.findViewById(R.id.dialogButtonCancelar);
        dialogButtonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ContainerActivity) activity).setSessionCanceled(adventure, session,
                        getAdapterPosition(), (finalEstado == 0));
                dialog.dismiss();
            }
        });

        Calendar dataAtual = Calendar.getInstance();
        dataAtual.add(Calendar.DATE, -1);

        if(dataAtual.after(LoginActivity.stringToDate(session.getData(), "dd/MM/yyyy"))) {
            dialogButtonConfirmar.setVisibility(View.GONE);
            dialogButtonCancelar.setVisibility(View.GONE);
        } else {
            if (estado == 0) {
                dialogButtonConfirmar.setVisibility(View.GONE);
            }
            if ((estado == 2) || adventure.getMestre().equals(uid)) {
                dialogButtonCancelar.setVisibility(View.GONE);
            }
        }

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private String getUsername(String id){
        return ((ContainerActivity) activity).getUserList().get(id).getUsername();
    }
}
