package tw.org.iii.android201919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.net.NetworkInterface;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager connectivityManager;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.v("brad", "network = " + isConnectNetwork());
        Log.v("brad", "wifi = " + isWifiConnect());

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver, filter);

    }

    @Override
    public void finish() {
        super.finish();
        unregisterReceiver(myReceiver);
    }

    private boolean isConnectNetwork(){
        boolean ret = false;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null){
            ret = false;
        }else{
            ret = info.isConnected();
        }
        return ret;
    }

    private boolean isWifiConnect(){
        boolean ret = false;
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info == null){
            ret = false;
        }else{
            ret = info.isConnected();
        }
        return ret;
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("brad", "network = " + isConnectNetwork());
            Log.v("brad", "wifi = " + isWifiConnect());
        }
    }

}
