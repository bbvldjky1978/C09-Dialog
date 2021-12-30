package tw.oldpa.m0904;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class M0904 extends AppCompatActivity  implements
        View.OnClickListener,
        SensorEventListener {

    //   <!--------設定G力所使用到的變數--------->
    private static final float ShakeThresholdGravity = 1.5f;//重力閾值  1次要甩多大力
    private static final int ShakeSlopTimeMs = 1000; //甩1次要在1.0秒內完成才算1次
    private static final int ShakeCountResetTimeMs = 5000;//多久沒甩 計數器會重製
    private Button B001;
    private long firstclick = 0, secondclick = 0;
    private int iDiffSec = 1;
    private Handler proBarHandler = new Handler();
    private int t;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private M0904 mShakeDetector;

    private Vibrator mVibrator;
    //<----------------使用加速度計會使用到的變數------------------->
    private OnShakeListener mListener;//搖動監聽器

    private long mShakeTimestamp;//暫存開始搖動的系統時間
    private int mShakeCount = 0;//紀錄搖動次數的計數器
    private int distime=2000;
//    private final ProgressDialog proBarDialog = new ProgressDialog(M0904.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m0904);
        setupviewcomponent();
    }

    private void setupviewcomponent() {
        B001 = (Button) findViewById(R.id.m0904_b001);
//        B001.setOnClickListener(this);      //按鈕監聽不用

        //--------------------------------------------------------------------
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//開啟感測服務
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//將加速度計設定給mAccelerometer使用
        mShakeDetector = new M0904(); //mShakeDetector繼承M0904這個class

        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {
                mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//取得系統Vibrator服務控制
                mVibrator.vibrate(1000);//震動0.5秒
//                Toast.makeText(getApplicationContext(), "Shake:" + count, Toast.LENGTH_SHORT).show();
                if (count % 2 == 0) {
                    domywork();
                }
            }
        }
        );
    }
//    <!---------------自建立搖動監聽器----------------------->
    private interface OnShakeListener { //宣告一個發生搖動時候的監聽器
        public void onShake(int count);
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - firstclick < distime) {
            if (SystemClock.elapsedRealtime() - secondclick < distime) {
                domywork();
                return;
            }
            secondclick = SystemClock.elapsedRealtime();
            return;
        }
        firstclick = SystemClock.elapsedRealtime();
    }


    protected void domywork() {
        final ProgressDialog proBarDialog = new ProgressDialog(this);
        proBarDialog.setTitle(getString(R.string.m0904_title));
        proBarDialog.setMessage(getString(R.string.m0904_message));
        proBarDialog.setIcon(android.R.drawable.star_big_on);
        proBarDialog.setCancelable(false);
        proBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        proBarDialog.setMax(100);
        proBarDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar begin = Calendar.getInstance();
                do {

                    Calendar now = Calendar.getInstance();
                    iDiffSec = (60 * now.get(Calendar.MINUTE) - 60 * begin.get(Calendar.MINUTE))
                            + (now.get(Calendar.SECOND) - begin.get(Calendar.SECOND));
                    if (iDiffSec * 10 > 100) {
                        proBarHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                proBarDialog.setProgress(100);
                            }
                        });
                        break;
                    }
                    proBarHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            proBarDialog.setProgress(iDiffSec * 10);
                        }
                    });
//

                } while (true);
                proBarDialog.cancel();
                //--------------------
                firstclick = 0;
                secondclick = 0;
                t = 1;
                mShakeCount = 0;
                //------------------
            }
        }).start();
    }

    public void setOnShakeListener(OnShakeListener listener) {  //宣告設定搖動監聽器的method
        this.mListener = listener;
    }

    //<---------------------------------------------------->
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
//            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);
            double gForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);
            if (gForce > ShakeThresholdGravity) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + ShakeSlopTimeMs > now) {
                    return;
                }
                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + ShakeCountResetTimeMs < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                mListener.onShake(mShakeCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        super.onResume(); //在resume週期的時候持續監聽
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);//在pause週期的時候停止監聽

    }


}
