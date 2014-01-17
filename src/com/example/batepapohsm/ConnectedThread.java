package com.example.batepapohsm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class ConnectedThread extends Thread {
	private final BluetoothSocket bSocket;
	private final InputStream iStream;
	private final OutputStream oStream;
	private final Handler bHandler = new Handler();
	
	
	public ConnectedThread(BluetoothSocket bs, Context context) {
		startNewActivity(context);
		
		bSocket = bs;
		InputStream in = null;
		OutputStream out = null;
		
		try {
			in = bSocket.getInputStream();
			out = bSocket.getOutputStream();
		} catch (IOException e) {}
		
		iStream = in;
		oStream = out;
	}
	
	private void startNewActivity(Context context) {
		Intent intent = new Intent(context, DialogActivity.class);
		context.startActivity(intent);
	}

	public void run() {
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while (true) {
			try {
				bytes = iStream.read(buffer);
				bHandler.obtainMessage(2, bytes, -1, buffer)
					.sendToTarget();
			} catch (IOException e) {
				break;
			}
		}
	}
	
	public void write(byte message[]) {
		try {
			oStream.write(message);
		} catch (IOException e) {}
	}
	
	public void cancel() {
		try {
			bSocket.close();
		} catch (IOException e) {}
	}
}
