package com.example.bbnsu_000.pokerwifip2p.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bbnsu_000.pokerwifip2p.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StartScreenFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button create_group_button ;
    private Button join_group_button;

    public StartScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_screen, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        create_group_button = (Button) getActivity().findViewById(R.id.create_group_button);
        join_group_button = (Button) getActivity().findViewById(R.id.join_group_button);

        create_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call methods in main activity to create a group
                mListener.onCreateGroupButton();
            }
        });

        join_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //cal methods in main activity to join a group
                mListener.onJoinGroupButton();
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
        public void onCreateGroupButton();
        public  void onJoinGroupButton();
    }

}
