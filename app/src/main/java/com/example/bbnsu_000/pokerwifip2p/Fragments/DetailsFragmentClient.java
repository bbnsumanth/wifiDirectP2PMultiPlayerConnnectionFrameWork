package com.example.bbnsu_000.pokerwifip2p.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bbnsu_000.pokerwifip2p.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragmentClient.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailsFragmentClient extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button start_button;
    private EditText name;
    private EditText buyin;

    public DetailsFragmentClient() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_fragment_client, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        start_button = (Button) getActivity().findViewById(R.id.create_group_button);
        name = (EditText) getActivity().findViewById(R.id.name_edit);
        buyin = (EditText) getActivity().findViewById(R.id.buyin_edit);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send name and buyin to activity
                //and change fragment to ClientList Fragment
                mListener.onClientStartButton(name.getText().toString(), Integer.valueOf(buyin.getText().toString()));
            }
        });

    }
    

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onClientStartButton(String name,int buyin);
    }

}
