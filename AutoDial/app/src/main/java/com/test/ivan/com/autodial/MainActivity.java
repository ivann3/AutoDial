package com.test.ivan.com.autodial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.content.Context;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.telephony.ITelephony;


public class MainActivity extends ActionBarActivity {

    private Switch dialSwitch;

    private EditText numberEditText;

    private EditText timesEditText;

    private static MyThread myThread=null;

    private static Timer timer = null;

    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInit();

        /*
          注册监听 */
        TelephonyManager mTelephonyManager = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
//        mTelephonyManager.listen(phoneStateListener,
//                PhoneStateListener.LISTEN_CALL_STATE);

       dialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               timer = new Timer();

               if(isChecked){

                   int period = getTimes();

                   timer.schedule(timerTask, 1000, period * 1000);
               }else {

               }
           }
       });

    }


    private void layoutInit(){

        dialSwitch = (Switch)findViewById(R.id.switch1);

        numberEditText = (EditText)findViewById(R.id.editText);

        timesEditText = (EditText) findViewById(R.id.editText2);
    }


    private String getNumber(){

        return numberEditText.getText().toString().trim();

    }

    private int getTimes(){

        times = Integer.valueOf(timesEditText.getText().toString().trim());

        return times;
    }

    private void dialPlan(String number){

        Intent dialIntent = new Intent(Intent.ACTION_CALL);

        String str = "tel:" + number;

        Log.d("Str",str);

        dialIntent.setData(Uri.parse(str));

        this.startActivity(dialIntent);
    }

    class  MyThread extends Thread{

        public void run() {

            ITelephony iTelephony = getITelephony(MainActivity.this);

            String number = getNumber();

            dialPlan(number);

            try {
                Thread.sleep(10000);

                iTelephony.endCall();

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    TimerTask timerTask =new TimerTask() {
        @Override
        public void run() {

            myThread = new MyThread();

            myThread.start();

//            times--;
        }
    };




    /**)
     * @param context
     * @return
     * 获得telephony的实例
     */
    private static ITelephony getITelephony(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(TELEPHONY_SERVICE);
        Class c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
                    (Class[]) null); // 获取声明的方法
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(
                    mTelephonyManager, (Object[]) null); // 获取实例
            return iTelephony;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


/*
    PhoneStateListener phoneStateListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            ITelephony iTelephony = getITelephony(MainActivity.this);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    iTelephony = getITelephony(getApplicationContext()); //获取电话接口
                    if (iTelephony != null) {
                        try {
                            iTelephony.endCall(); // 挂断电话
                            Message msg = new Message();
                            msg.what = SYS_MSG;
                            msg.obj ="成功挂断["+incomingNumber+"]的电话";
                            handler.sendMessage(msg);
                        } catch (RemoteException e) {
                            Message msg = new Message();
                            msg.what = SYS_MSG;
                            msg.obj ="挂断["+incomingNumber+"]的电话失败";
                            handler.sendMessage(msg);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };
*/

    @Override
    public void onDestroy(){
        super.onDestroy();

        if (timer != null){

            timer.cancel();

            timer=null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
