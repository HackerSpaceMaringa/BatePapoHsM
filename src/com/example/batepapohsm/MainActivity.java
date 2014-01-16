package com.example.batepapohsm;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {
	private BluetoothAdapter bAdapter;
	private RelativeLayout relLay;
	private ListView lview;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> devices = new ArrayList<String>();
	private ArrayList<BluetoothDevice> devices_object = new ArrayList<BluetoothDevice>();

	private final BroadcastReceiver bRec = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice bDev = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				devices.add(bDev.getName());
				adapter.notifyDataSetChanged();
				devices_object.add(bDev);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bAdapter = BluetoothAdapter.getDefaultAdapter();
		relLay = (RelativeLayout) findViewById(R.id.relLay);
		lview = (ListView) findViewById(R.id.lView);
		
		Button bt = (Button) findViewById(R.id.btScan);
		bt.setOnClickListener(this);

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(bRec, filter);
		
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.tvDev, devices);
		lview.setAdapter(adapter);
		lview.setOnItemClickListener(this);
		
		AcceptThread acT = new AcceptThread(bAdapter);
		acT.start();
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
		if (bAdapter != null) {
			if (!devices.isEmpty()) {
				devices.clear();
				devices_object.clear();
				adapter.notifyDataSetChanged();
			}
			if (!bAdapter.startDiscovery()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("Shit");
				builder.setTitle("Nope");
				builder.create().show();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View what, int index, long id) {
		ConnectThread cntT = new ConnectThread(devices_object.get(index));
		cntT.start();
	}

}
