package com.example.batepapohsm;

import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private BluetoothAdapter bAdapter;
	private LinearLayout layout;
	private LinkedList<BluetoothDevice> list = new LinkedList<BluetoothDevice>();
	
	private final BroadcastReceiver bRec = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) { 
				BluetoothDevice bDev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				list.add(bDev);
			}
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout)findViewById(R.id.linLay);
		bAdapter = BluetoothAdapter.getDefaultAdapter();
        
        Button bt = (Button)findViewById(R.id.btScan);
        bt.setOnClickListener(this);
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bRec, filter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(bAdapter != null) {
			bAdapter.startDiscovery();
			TextView tv;
			for (BluetoothDevice d : list) {
				tv = new TextView(this);
				tv.setText(d.getName());
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				layout.addView(tv);
			}
		} else {
			Log.d("BatePapo", "NotHave");
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		bAdapter.cancelDiscovery();
		unregisterReceiver(bRec);
		super.onDestroy();
	}
    
}
