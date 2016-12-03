package be.howest.nmct.celebmatch.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.howest.nmct.celebmatch.R;

public class ResultFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final String PREDICTION_RESULT="predict_result";
    private String mResultToShow;
    private TextView txtViewResult;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(String prediction) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(PREDICTION_RESULT,prediction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResultToShow=getArguments().getString(PREDICTION_RESULT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_result, container, false);
        txtViewResult=(TextView) myView.findViewById(R.id.textViewResult);
        txtViewResult.setText("You look most like: "+mResultToShow);
        return myView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement iPhotoFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }
}
