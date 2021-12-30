package tw.tcnr08.m0902;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class M0902 extends AppCompatActivity implements View.OnClickListener {

    private Button btn001,btn002;
    private TextView txt001;
//    final String[] listStr={"張三","李四","王五","陳六","呂七","宋八"};


    final String[] ListStr = {"張三", "李四", "王五", "陳六", "呂七", "宋八",
            "如果選擇項目太多", "Android也會", "自動的可以拖曳喔！～", "李四", "王五", "陳六", "呂七", "宋八","張三", "李四", "王五", "陳六", "呂七", "宋八",
            "如果選擇項目太多", "Android也會", "自動的可以拖曳喔！～", "李四", "王五", "陳六", "呂七", "宋八"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m0902);
        setupviewcomponetn();

    }

    private void setupviewcomponetn() {
        btn001 = (Button) findViewById(R.id.m0902_b001);
        btn002 = (Button) findViewById(R.id.m0902_b002);

        txt001 = (TextView) findViewById(R.id.m0902_t001);

        btn001.setOnClickListener(this);
        btn002.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        txt001.setText("");

        switch (v.getId()){
            case R.id.m0902_b001:
                MyAlertDialog myAlertDialog = new MyAlertDialog(M0902.this);

                myAlertDialog.setTitle(getString(R.string.m0902_title));  //Dialog四個
                myAlertDialog.setMessage(getString(R.string.m0902_t001)+getString(R.string.m0902_b001));
                myAlertDialog.setIcon(android.R.drawable.star_big_on);
                myAlertDialog.setCancelable(true);

                myAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE,getString(R.string.m0902_positive),Alt01On);
                myAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,getString(R.string.m0902_negative),Alt01On);
                myAlertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,getString(R.string.m0902_neutral),Alt01On);



                myAlertDialog.show();
                break;

//                -------------這是監聽內容-------
            case R.id.m0902_b002:
                AlertDialog.Builder myAlertDialog02 = new AlertDialog.Builder(this);

                myAlertDialog02.setTitle(getString(R.string.m0902_title));
                myAlertDialog02.setIcon(android.R.drawable.star_big_off);
                myAlertDialog02.setCancelable(false);

//-----------------------
                myAlertDialog02.setItems(ListStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "select:" + ListStr[which], Toast.LENGTH_SHORT).show();
                        //--可進行不同的處理方法
                        txt001.setText(getString(R.string.m0902_t001) +
                                getString(R.string.m0902_b002) + "\n" +
                                getString(R.string.m0902_click) + " " + ListStr[which]);


                    }
                });

//                ----------------監聽按鈕------------------

                myAlertDialog02.setPositiveButton(getString(R.string.m0902_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        txt001.setText(getString(R.string.m0902_t001) +
                                getString(R.string.m0902_b002) +
                                getString(R.string.m0902_click) + " " +
                                getString(R.string.m0902_positive) + " " +
                                getString(R.string.m0902_button));


                    }

                });
//---------------------

                myAlertDialog02.setNegativeButton(getString(R.string.m0902_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        txt001.setText(getString(R.string.m0902_t001) +
                                getString(R.string.m0902_b002) +
                                getString(R.string.m0902_click) + " " +
                                getString(R.string.m0902_negative) + " " +
                                getString(R.string.m0902_button));


                    }

                });

                myAlertDialog02.setNeutralButton(getString(R.string.m0902_neutral), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        txt001.setText(getString(R.string.m0902_t001) +
                                getString(R.string.m0902_b002) +
                                getString(R.string.m0902_click) + " " +
                                getString(R.string.m0902_neutral) + " " +
                                getString(R.string.m0902_button));


                    }

                });






                myAlertDialog02.show();
                break;
        }


    }


    private DialogInterface.OnClickListener Alt01On=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which){
                case -1:
                    txt001.setText(getString(R.string.m0902_t001) +
                            getString(R.string.m0902_b001) +
                            getString(R.string.m0902_click) + " [" +
                            getString(R.string.m0902_positive) + "] " +
                            getString(R.string.m0902_button));


                    break;
                case -2:
                    txt001.setText(getString(R.string.m0902_t001) +
                            getString(R.string.m0902_b001) +
                            getString(R.string.m0902_click) + " [" +
                            getString(R.string.m0902_negative) + "] " +
                            getString(R.string.m0902_button));


                    break;
                case -3:
                    txt001.setText(getString(R.string.m0902_t001) +
                            getString(R.string.m0902_b001) +
                            getString(R.string.m0902_click) + " [" +
                            getString(R.string.m0902_neutral) + "] " +
                            getString(R.string.m0902_button));


                    break;
            }




        }
    };

}