package br.com.yimobile.igor.screens.container.settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.yimobile.igor.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.fragment_settings, null);

        return v;
    }
}
