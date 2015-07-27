package org.itstep.mushta.football;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.itstep.mushta.football.R;


public class AddCommandFragment extends Fragment {

    public AddCommandFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_command_fragment, container,
                false);

        return rootView;
    }

}