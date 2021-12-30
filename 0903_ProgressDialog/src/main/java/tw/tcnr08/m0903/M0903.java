package tw.tcnr08.m0903;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class M0903 extends AppCompatActivity implements View.OnClickListener {

    private Button b001;
    private Handler mHandler = new Handler();
//    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m0903);
        setupviewcomponent();

    }

    private void setupviewcomponent() {
        b001 = (Button) findViewById(R.id.m0903_b001);

        b001.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final ProgressDialog pDialog = new ProgressDialog(this);

        pDialog.setTitle(getString(R.string.m0903_title));
        pDialog.setMessage(getString(R.string.m0903_message));
        pDialog.setIcon(android.R.drawable.star_big_on);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //不寫就圓圈
        pDialog.setMax(50);


        pDialog.show();
//        --------------------------

        new Thread(new Runnable() {
            public void run() {
                Calendar begin = Calendar.getInstance();
                do {
                    Calendar now = Calendar.getInstance();
                    final int iDiffSec = 60 * (now.get(Calendar.MINUTE) - begin.get(Calendar.MINUTE)) +
                            (now.get(Calendar.SECOND) - begin.get(Calendar.SECOND));
//-------------------------------------
                    if (iDiffSec * 2 > 100) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                pDialog.setProgress(100);
                            }
                        });
                        break;
                    }
//-------------------------------------
                    mHandler.post(new Runnable() {
                        public void run() {
                            pDialog.setProgress(iDiffSec * 2); //1,2,4,5
                        }
                    });
//-------------------------------------
                    if ((iDiffSec * 4) < 100){
                        mHandler.post(new Runnable() {
                                          public void run() {
                                              pDialog.setSecondaryProgress(iDiffSec * 4);
                                          }
                                      }
                        );
                    }else{
                        mHandler.post(new Runnable() {
                            public void run() {
                                pDialog.setSecondaryProgress(100);
                            }
                        });
                    }

                } while (true);
                pDialog.cancel(); //對話盒關掉
            }
        }).start();

    }
}