package be.howest.nmct.celebmatch.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import be.howest.nmct.celebmatch.MainActivity;
import be.howest.nmct.celebmatch.R;

public class SettingsFragment extends Fragment {

    private Button btnSave;
    private Context mContext;
    private settingsListener mListener;
    private EditText editTextIP;
    private MainActivity mActivity;
    public final static String IP_SETTING="serverIp";

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_settings, container, false);
        btnSave=(Button) myView.findViewById(R.id.btnSave);
        editTextIP=(EditText) myView.findViewById(R.id.editTextIP);
        editTextIP.setText(mActivity.getIP());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip=editTextIP.getText().toString();
                saveIpToSettings(ip);
            }
        });
        return myView;
    }

    private void saveIpToSettings(String ip){
        if(mActivity.getIP().equals(ip)){
            Snackbar.make(mActivity.findViewById(R.id.frameLayoutMain), "IP was already saved", Snackbar.LENGTH_LONG).show();
        }else {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(IP_SETTING,ip);
            editor.commit();
            mListener.setNewSavedIp(ip);
            Snackbar.make(mActivity.findViewById(R.id.frameLayoutMain), "Settings saved", Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity=(MainActivity)context;
        this.mContext=context;
        if (context instanceof settingsListener) {
            mListener = (settingsListener) context;
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

    public interface settingsListener{
        void setNewSavedIp(String newIP);
    }

}
