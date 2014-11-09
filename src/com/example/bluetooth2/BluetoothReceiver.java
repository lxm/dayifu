package com.example.bluetooth2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothReceiver extends BroadcastReceiver {

	private static int count = 0;
	private static int[] isMove = new int[30];

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		// System.out.println("ccccc");
		if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
			// displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			// System.out.println("BroadcastReceiver onData:"
			// + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			String res_num = intent
					.getStringExtra(BluetoothLeService.EXTRA_DATA);
			String res_num_str[] = res_num.split(",");
			if (res_num_str.length <= 5) {
				return;
			}
			for (int i = 0; i < 6; i++) {
				if (res_num_str[i].equals("-")) {
					return;
				}
			}
			double res_num_doub[] = new double[6];
			for (int i = 0; i < 6; i++) {
				res_num_doub[i] = Double.valueOf(res_num_str[i]);
			}
			Movement moveJudge = new Movement();
			double[] moveresult = moveJudge.GetType(res_num_doub);
			// System.out.println("1: " + moveresult[0] + " 2: "
			// + moveresult[1] + " 3: " + moveresult[2]);

			isMove[count] = getMax(moveresult);
			System.out.println("count: " + count + ". 谁最大： " + isMove[count]);

			count++;
			if (count >= 30) {
				int num1 = 0;
				int num2 = 0;
				int num3 = 0;
				for (int i = 0; i < 30; i++) {
					if (isMove[i] == 0) {
						num1++;
					} else if (isMove[i] == 1) {
						num2++;
					} else {
						num3++;
					}
				}

				if (num1 > num2 && num1 > num3) {
					System.out.println("num1最多");
				} else if (num2 > num3) {
					System.out.println("num2最多");
				} else {
					System.out.println("num3最多");
				}

				count = 0;
			}
		}
	}

	private int getMax(double[] doub) {
		double max = 0;
		int num = 0;
		if (doub[0] > doub[1]) {
			max = doub[0];
			num = 0;
		} else {
			max = doub[1];
			num = 1;
		}

		if (doub[2] > max) {
			max = doub[2];
			num = 2;
		}
		return num;
	}

}
