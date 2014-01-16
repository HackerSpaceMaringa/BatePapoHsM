package com.example.batepapohsm;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ConnectThread extends Thread {
	private BluetoothSocket bSocket;
	
	public ConnectThread(BluetoothDevice bDevice) {
		BluetoothSocket tmp = null;
		try {
			tmp = bDevice.createRfcommSocketToServiceRecord(
					UUID.fromString(UUIDbluetooth.UUID_Secure));
		} catch (Exception e) {}
		bSocket = tmp;
	}
	
	public void run() {
		try {
			bSocket.connect();
		} catch (IOException e) {
			try {
				bSocket.close();
			} catch (IOException e1) {}
			return;
		}
	}
	
	public void cancel() {
		try {
			bSocket.close();
		} catch (IOException e) {}
	}
}
