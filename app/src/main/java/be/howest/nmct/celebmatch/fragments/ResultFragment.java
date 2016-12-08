package be.howest.nmct.celebmatch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import be.howest.nmct.celebmatch.R;

public class ResultFragment extends Fragment {

    private onResultFragmentListener mListener;
    private static final String PREDICTION_RESULT="predict_result";
    private String mResultToShow;
    private TextView txtViewResult;
    private TextView txtViewSecondResult;
    private String topResultActorName;
    private ArrayList<String> resultStrings;
    private ImageView imgViewActor;

    public ResultFragment() {
        // Required empty public constructor
    }

    private ArrayList<String> parseServerJSON(String json) throws JSONException {
        ArrayList<String> resultArray= new ArrayList<String>();
        JSONObject obj=new JSONObject(json);
        JSONArray array=obj.getJSONArray("actors");
        for(int i=0,len=array.length();i<3;i++){
            JSONObject subObj=array.getJSONObject(i);
            String name=subObj.getString("name");
            if(i==0){
                topResultActorName=name;
            }
            String score=subObj.getString("score");
            Double dScore=Double.parseDouble(score);
            dScore=dScore*100;
            String result=name+" "+String.valueOf(dScore)+"% match";
            resultArray.add(result);
        }
        return resultArray;
    }

    private void showActor(){
        switch (topResultActorName){
            case "angelina jolie":
                imgViewActor.setImageResource(R.drawable.angelina_jolie);
                break;
            case "jennifer aniston":
                imgViewActor.setImageResource(R.drawable.jennifer_aniston);
                break;
            case "natalie portman":
                imgViewActor.setImageResource(R.drawable.natalie_portman);
                break;
            case "emma watson":
                imgViewActor.setImageResource(R.drawable.emma_watson);
                break;
            case "tom cruise":
                imgViewActor.setImageResource(R.drawable.tom_cruise);
                break;
            case "brad pitt":
                imgViewActor.setImageResource(R.drawable.brad_pitt);
                break;
            case "will smith":
                imgViewActor.setImageResource(R.drawable.will_smith);
                break;
            case "jennifer lawrence":
                imgViewActor.setImageResource(R.drawable.jennifer_lawrence);
                break;
            case "kate winslet":
                imgViewActor.setImageResource(R.drawable.kate_winslet);
                break;
            case "scarlett johansson":
                imgViewActor.setImageResource(R.drawable.scarlett_johansson);
                break;
            case "emilia clarke":
                imgViewActor.setImageResource(R.drawable.emilia_clarcke);
                break;
            case "russell crowe":
                imgViewActor.setImageResource(R.drawable.russel_crowe);
                break;
            case "tom hanks":
                imgViewActor.setImageResource(R.drawable.tom_hanks);
                break;
            case "sandra bullock":
                imgViewActor.setImageResource(R.drawable.sandra_bullock);
                break;
            case "heath ledger":
                imgViewActor.setImageResource(R.drawable.heath_ledger);
                break;
            case "mila kunis":
                imgViewActor.setImageResource(R.drawable.mila_kunis);
                break;
            case "johnny depp":
                imgViewActor.setImageResource(R.drawable.johnny_depp);
                break;
            case "anna gunn":
                imgViewActor.setImageResource(R.drawable.anna_gunn);
                break;
            case "leonardo dicaprio":
                imgViewActor.setImageResource(R.drawable.leonardo_dicaprio);
                break;
            case "robert de niro":
                imgViewActor.setImageResource(R.drawable.robert_deniro);
                break;

        }
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
        txtViewSecondResult=(TextView) myView.findViewById(R.id.textViewSecondaryResult);
        imgViewActor=(ImageView) myView.findViewById(R.id.resultImage);
        try {
            resultStrings=parseServerJSON(mResultToShow);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showActor();
        txtViewResult.setText(resultStrings.get(0));
        txtViewSecondResult.setText("You also look like "+resultStrings.get(1)+" and "+resultStrings.get(2));
        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onResultFragmentListener) {
            mListener = (onResultFragmentListener) context;
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

    public interface onResultFragmentListener {

    }
}
