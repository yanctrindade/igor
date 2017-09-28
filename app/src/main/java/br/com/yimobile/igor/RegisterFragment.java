package br.com.yimobile.igor;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RegisterFragment extends Fragment {
    private OnRegisterInteractionListener mCallback;
    private EditText emailText, senhaText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailText = (EditText) view.findViewById(R.id.email);
        senhaText = (EditText) view.findViewById(R.id.senha);
        view.findViewById(R.id.cadastrobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation wiggle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wiggle);
                String email = emailText.getText().toString();
                String senha = senhaText.getText().toString();

                if(email.isEmpty() || email.equals("")) {
                    emailText.startAnimation(wiggle);
                    emailText.setError("Preencha seu email");
                }
                else if(!email.contains("@") || !email.contains(".")) {
                    emailText.startAnimation(wiggle);
                    emailText.setError("Email inválido");
                }
                else if(senha.isEmpty() || senha.equals("")) {
                    senhaText.startAnimation(wiggle);
                    senhaText.setError("Preencha sua senha");
                }
                else if (mCallback != null) {
                    mCallback.onRegisterInteraction(email, senha);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnRegisterInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface OnRegisterInteractionListener {

        public void onRegisterInteraction(String email, String senha);

    }

}
