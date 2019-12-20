package tw.org.iii.android201919;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;
    private AccountManager accountManager;
    private MyReceiver myReceiver;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED
        ) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_CALL_LOG,},
                        123);

        } else {
            // Permission has already been granted
            init();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        Log.v("brad", "network = " + isConnectNetwork());
//        Log.v("brad", "wifi = " + isWifiConnect());

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver, filter);

        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Log.v("brad", telephonyManager.getImei());
        Log.v("brad", telephonyManager.getMeid());
        Log.v("brad", telephonyManager.getSubscriberId());

        accountManager = (AccountManager)getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccounts();
        for (Account account : accounts){
            try {
                String type = account.type;
                String name = account.name;
                String passwd = accountManager.getPassword(account);
                Log.v("brad", type + ":" + name);
            }catch (Exception e){

            }
        }

        contentResolver =getContentResolver();

        //====
        Uri uri = CallLog.Calls.CONTENT_URI;    // SQLIte Table
        Cursor c = contentResolver.query(uri, null,null,null, null);
        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String tel = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String type= c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
            //String du = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
            long date = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date cdate = new Date(date);
            ;

            Log.v("brad", name + ":" + tel + ":" + type + " => " + df.format(cdate));
        }

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
//            Log.v("brad", "network = " + isConnectNetwork());
//            Log.v("brad", "wifi = " + isWifiConnect());
        }
    }

}
