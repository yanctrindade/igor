package br.com.yimobile.igor.screens.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import br.com.yimobile.igor.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment {
    private OnLoginInteractionListener mCallback;
    private EditText emailText, senhaText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailText = view.findViewById(R.id.email);
        senhaText = view.findViewById(R.id.senha);
        view.findViewById(R.id.enterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

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
                else if(mCallback != null) {
                    mCallback.onLoginInteraction(email, senha);
                }
            }
        });

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnLoginInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    interface OnLoginInteractionListener {

        public void onLoginInteraction(String email, String senha);

    }

}
