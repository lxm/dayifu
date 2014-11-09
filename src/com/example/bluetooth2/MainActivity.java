package com.example.bluetooth2;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private static int count = 0;
	private static int[] isMove = new int[30];
	protected static final String TAG = null;
	int REQUEST_ENABLE_BT = 1;
	//private static BluetoothLeService mBluetoothLeService;
	
	//private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
     // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙出现错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        /*
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        registerReceiver(mGattUpdateReceiver, intentFilter);
        */
    }

    
private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			// System.out.println("ccccc");
			if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				// displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
				//System.out.println("BroadcastReceiver onData:"
				//		+ intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
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
				//System.out.println("1: " + moveresult[0] + " 2: "
				//		+ moveresult[1] + " 3: " + moveresult[2]);
				
				isMove[count] = getMax(moveresult);
				System.out.println("count: " + count + ". 谁最大： " + isMove[count]);
				
				count++;
				if(count >= 30){
					int num1 = 0;
					int num2 = 0;
					int num3 = 0;
					for(int i = 0; i < 30; i++){
						if(isMove[i] == 0){
							num1++;
						}else if(isMove[i] == 1){
							num2++;
						}else{
							num3++;
						}
					}
					
					if(num1 > num2 && num1 > num3){
						System.out.println("num1最多");
					}else if(num2 > num3){
						System.out.println("num2最多");
					}else{
						System.out.println("num3最多");
					}
						
					count = 0;
				}
			}
		}
		
		private int getMax(double[] doub){
			double max = 0;
			int num = 0;
			if(doub[0] > doub[1]){
				max = doub[0];
				num = 0;
			}else{
				max = doub[1];
				num = 1;
			}
			
			if(doub[2] > max){
				max = doub[2];
				num = 2;
			}
			return num;
		}
	};
}
