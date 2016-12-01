package be.howest.nmct.celebmatch.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.howest.nmct.celebmatch.MainActivity;
import be.howest.nmct.celebmatch.R;

public class HomeFragment extends Fragment {

    private Button btnPhoto;
    private iHomeFragmentListener mListener;
    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(iHomeFragmentListener listener) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void setListener(iHomeFragmentListener listener){
        this.mListener=listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_home, container, false);
        btnPhoto=(Button) myView.findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.switchToPhotoFragment(MainActivity.fragmentIds[1]);
            }
        });
        return myView;
    }

    public interface iHomeFragmentListener{
        void switchToPhotoFragment(String id);
    }

}
