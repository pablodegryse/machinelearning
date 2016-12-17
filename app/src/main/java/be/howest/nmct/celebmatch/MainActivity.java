package be.howest.nmct.celebmatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import be.howest.nmct.celebmatch.fragments.HomeFragment;
import be.howest.nmct.celebmatch.fragments.PhotoFragment;
import be.howest.nmct.celebmatch.fragments.ResultFragment;
import be.howest.nmct.celebmatch.fragments.SettingsFragment;


public class MainActivity extends AppCompatActivity implements HomeFragment.iHomeFragmentListener
        ,PhotoFragment.iPhotoFragmentListener,ResultFragment.onResultFragmentListener
        ,SettingsFragment.settingsListener{

    private FrameLayout frameLayoutMain;
    public static final String[] fragmentIds={"home","photo","result"};
    private String savedIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("dikke tetjes");
        frameLayoutMain=(FrameLayout) findViewById(R.id.frameLayoutMain);
        switchFragment(fragmentIds[0],"");
        this.savedIP=getIPfromSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tools, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("setting ip","to..........");
                switchFragment("settings",null);
                return true;

            default: return true;
        }
    }

    public void switchFragment(String fragmentId,String result){
        FragmentManager fragManager= getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        switch(fragmentId){
            case "home":
                HomeFragment homeFrag = HomeFragment.newInstance(this);
                fragTransaction.replace(R.id.frameLayoutMain,homeFrag);
                fragTransaction.addToBackStack("settingsTransaction");
                fragTransaction.commit();
                break;
            case "photo":
                PhotoFragment photoFrag = new PhotoFragment();
                fragTransaction.replace(R.id.frameLayoutMain,photoFrag);
                fragTransaction.addToBackStack("settingsTransaction");
                fragTransaction.commit();
                break;
            case "result":
                ResultFragment resultFrag = ResultFragment.newInstance(result);
                fragTransaction.replace(R.id.frameLayoutMain,resultFrag);
                fragTransaction.addToBackStack("settingsTransaction");
                fragTransaction.commit();
                break;
            case "settings":
                SettingsFragment setFrag=new SettingsFragment();
                fragTransaction.replace(R.id.frameLayoutMain,setFrag);
                fragTransaction.addToBackStack("settingsTransaction");
                fragTransaction.commit();
        }
    }

    private String getIPfromSettings(){
        String resultIp="";
        SharedPreferences sharedPref =getPreferences(Context.MODE_PRIVATE);
        resultIp=sharedPref.getString(SettingsFragment.IP_SETTING,"192.168.1.100");
        return resultIp;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void switchToPhotoFragment(String id) {
        switchFragment(id,"");
    }

    @Override
    public void ShowResult(String result) {
        switchFragment(fragmentIds[2],result);
    }

    public String getIP(){
        return this.savedIP;
    }

    @Override
    public void setNewSavedIp(String newIP) {
        this.savedIP=newIP;
    }
}
