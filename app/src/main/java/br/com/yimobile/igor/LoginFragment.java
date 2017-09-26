package br.com.yimobile.igor;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginFragment extends Fragment {
    private OnLoginInteractionListener mCallback;
    private EditText emailText, senhaText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_login, container, false);

        emailText = (EditText) view.findViewById(R.id.email);
        senhaText = (EditText) view.findViewById(R.id.senha);
        view.findViewById(R.id.enterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String senha = senhaText.getText().toString();
                if (mCallback != null && !email.isEmpty() && !senha.isEmpty()) {
                    mCallback.onLoginInteraction(email, senha);
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

    public interface OnLoginInteractionListener {

        public void onLoginInteraction(String email, String senha);

    }

}
