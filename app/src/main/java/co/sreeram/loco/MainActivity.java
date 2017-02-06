package co.sreeram.loco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected TextView textView2,textView3;
    protected Button start,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Loco.class);
                startService(intent);
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Loco.class);
                stopService(intent);

            }
        });
        registerReceiver(uiUpdated, new IntentFilter("LOCATION_UPDATED"));
    }

    private BroadcastReceiver uiUpdated= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            textView2 = (TextView) findViewById((R.id.textView2));
            textView2.setText(intent.getExtras().getString("BBBB"));
            textView3 = (TextView) findViewById((R.id.textView3));
            textView3.setText("Distance = "+intent.getExtras().getString("DIST"));

        }
    };
}
