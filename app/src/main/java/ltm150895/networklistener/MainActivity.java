package ltm150895.networklistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(broadcastReceiver,  new IntentFilter("NetworkChangeReceiver"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.textView1)).setText(message);
        Toast.makeText(getBaseContext(), "onResume", Toast.LENGTH_SHORT).show();
    }

    private String message;
    private BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tag = "onReceive";
            Bundle bundle = intent.getExtras();
            message = bundle.getString("message");
            Log.d(tag, "MainActivity :" + message);
            ((TextView) findViewById(R.id.textView1)).setText(message);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
