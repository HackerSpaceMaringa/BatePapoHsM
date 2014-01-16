package com.example.batepapohsm;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class AcceptThread extends Thread {
	private BluetoothServerSocket bss;
	
	public AcceptThread(BluetoothAdapter bAdapter) {
		BluetoothServerSocket tmp = null;
		try {
			tmp = bAdapter.listenUsingRfcommWithServiceRecord(bAdapter.getName(), 
					UUID.fromString(UUIDbluetooth.UUID_Secure));
		} catch (Exception e) {}
		bss = tmp;
	}
	
	public void run() {
		BluetoothSocket bSocket = null;
		while (true) {
			try {
				bSocket = bss.accept();
			} catch (Exception e) {
				break;
			}
			
			if (bSocket != null) {
				break;
			}
		}
	}
	
	public void cancel() {
		try {
			bss.close();
		} catch (Exception e) {}
	}
}
