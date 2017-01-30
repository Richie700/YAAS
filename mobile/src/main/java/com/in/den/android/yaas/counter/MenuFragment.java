package com.in.den.android.yaas.counter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.in.den.android.yaas.R;
import com.in.den.android.yaas.YassActivity;
import com.in.den.android.yaas.YassFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends YassFragment {


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn = (Button)view.findViewById(R.id.btn_start);
        //addToBackStack() to put the fragment in back history.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new GemeEngineFragment(), YassActivity.GAMEFRAGMENT)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
