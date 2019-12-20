package tw.org.iii.android201919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import java.net.NetworkInterface;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.v("brad", "network = " + isConnectNetwork());
        Log.v("brad", "wifi = " + isWifiConnect());
    }

    private boolean isConnectNetwork(){
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info.isConnected();
    }

    private boolean isWifiConnect(){
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info.isConnected();
    }

}
