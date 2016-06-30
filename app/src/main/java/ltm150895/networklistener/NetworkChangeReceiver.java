package ltm150895.networklistener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String tag= "onReceive";
        int status = getConnectivityStatusInt(context);
        Log.d(tag, "new data");

        String resultData = "Hello";

        switch (status){
            case NETWORK_STATUS_MOBILE:{
                Log.d(tag, "NETWORK_STATUS_MOBILE");
                TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                Log.d(tag, "Type : " + tm.getNetworkType());
                showNotification(context, "Mobile", getNetworkClass(tm.getNetworkType()));
                resultData = "Mobile " + getNetworkClass(tm.getNetworkType());
                break;
            }
            case NETWORK_STATUS_NOT_CONNECTED:{
                Log.d(tag, "NETWORK_STATUS_NOT_CONNECTED");
                showNotification(context, "Not connected", ":/");
                resultData = "Not connected :/";
                break;
            }
            case NETWORK_STATUS_WIFI:{
                Log.d(tag, "NETWORK_STATUS_WIFI");
                WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                Log.d(tag, "LinkSpeed: " + wm.getConnectionInfo().getLinkSpeed());
                showNotification(context, "Wifi", "LinkSpeed: " + wm.getConnectionInfo().getLinkSpeed() + " mbps");
                resultData = "Wifi: " + wm.getConnectionInfo().getLinkSpeed() + " mbps";
                break;
            }
        }

        Intent i = new Intent("NetworkChangeReceiver");
        i.putExtra(tag, resultData);
        i.putExtra("message", resultData);
        context.sendBroadcast(i);

    }

    private void SendData(Context context, String name, String value){
        Intent intent2open = new Intent(context, MainActivity.class);
        intent2open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2open.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //String name = "KEY";
        //String value = "String you want to pass";
        intent2open.putExtra(name, value);
        context.startActivity(intent2open); //this opens the activity on new event
    }

    private void showNotification(Context context, String title, String content) {
                int notifyID = 1;

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setOngoing(true) // this makes a persistent notification
                        .setContentIntent(contentIntent)
                        //.setDefaults(Notification.DEFAULT_SOUND)
                        ;
        //mBuilder.setAutoCancel(true); // to dismiss
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyID, mBuilder.build());

    }


    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;
    private static final int NETWORK_STATUS_NOT_CONNECTED=0, NETWORK_STATUS_WIFI =1,NETWORK_STATUS_MOBILE=2;


    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                return TYPE_WIFI;
            }


            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    private static int getConnectivityStatusInt(Context context) {
        int conn = getConnectivityStatus(context);
        int status = 0;
        if (conn == TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == TYPE_MOBILE) {
            status =NETWORK_STATUS_MOBILE;
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }


    private String getNetworkClass(int networkType){
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }
}