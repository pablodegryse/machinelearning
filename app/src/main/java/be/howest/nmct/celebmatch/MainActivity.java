package be.howest.nmct.celebmatch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import be.howest.nmct.celebmatch.fragments.HomeFragment;
import be.howest.nmct.celebmatch.fragments.PhotoFragment;
import be.howest.nmct.celebmatch.fragments.ResultFragment;


public class MainActivity extends AppCompatActivity implements HomeFragment.iHomeFragmentListener
        ,PhotoFragment.iPhotoFragmentListener {

    private FrameLayout frameLayoutMain;
    public static final String[] fragmentIds={"home","photo","result"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayoutMain=(FrameLayout) findViewById(R.id.frameLayoutMain);
        switchFragment(fragmentIds[0]);
    }

    public void switchFragment(String fragmentId){
        FragmentManager fragManager= getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        switch(fragmentId){
            case "home":
                HomeFragment homeFrag = HomeFragment.newInstance(this);
                fragTransaction.replace(R.id.frameLayoutMain,homeFrag);
                fragTransaction.commit();
                break;
            case "photo":
                PhotoFragment photoFrag = new PhotoFragment();
                fragTransaction.replace(R.id.frameLayoutMain,photoFrag);
                fragTransaction.commit();
                break;
            case "result":
                ResultFragment resultFrag = new ResultFragment();
                fragTransaction.replace(R.id.frameLayoutMain,resultFrag);
                fragTransaction.commit();
                break;
        }
    }

    @Override
    public void switchToPhotoFragment(String id) {
        switchFragment(id);
    }
}
