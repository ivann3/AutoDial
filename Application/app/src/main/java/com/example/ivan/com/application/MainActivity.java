package com.example.ivan.com.application;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class MainActivity extends ActionBarActivity {

    private  static  String FILENAME = "1G.zip";
    private  static  String DELETEDNAME = "succeed.rar";
    private  int times;
    private  static File testFile;
    private  static Button button;
    private  static TextView textView1;
    private  static  TextView textView2;
    private  static EditText editText;
    private  static RadioGroup radioGroup;

    private  TaskAsync asyncTask = null ;

    private  static Logger logger = null ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logging();

        try {
            this.init();

        } catch (IOException e) {
            e.printStackTrace();
        }

        final Boolean externalStatus =this.isExternalWritabel();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId ==(R.id.bigRadiobuttom)){
                    FILENAME = "1G.zip";

                    testFile = MainActivity.this.getTestFileInSDcard(FILENAME);

                    textView1.setText(testFile.getAbsolutePath());

                    logger.debug("RadioButtonChanged:bigRadioButtom");

                    logger.debug("filePath:" + testFile.getAbsolutePath());


                }else{
                    FILENAME = "1M.zip";

                    testFile = MainActivity.this.getTestFileInSDcard(FILENAME);

                    textView1.setText(testFile.getAbsolutePath());

                    logger.debug("RadioButtonChanged:smallRadioButtom");

                    logger.debug("filePath:" + testFile.getAbsolutePath());

                }
            }
        });

        final Boolean testFileExists = this.isTestFileInSDCard(FILENAME);


        if (!externalStatus ){
            Toast.makeText(this,"请检查SDCard",Toast.LENGTH_LONG).show();

            logger.debug("ExternalStatus:False");

        } else {

            logger.debug("ExternalStatus:True");

            if (testFileExists){

                logger.debug("xx.zipExists:True");

                testFile = this.getTestFileInSDcard(FILENAME);

                textView1.setText(testFile.getAbsolutePath());

                logger.debug("filePath:" + testFile.getAbsolutePath());

            }else {

                Toast.makeText(this,"请将" + FILENAME +"文件放在SDCard根目录",Toast.LENGTH_LONG).show();

                logger.debug("xx.zip File Not Found");

            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logger.debug("Start Button Click");

                if (testFileExists && externalStatus) {

                    setTimes(editText.getText().toString());

                    asyncTask = new TaskAsync(MainActivity.this, button, textView2, testFile, DELETEDNAME, times);

                    asyncTask.execute(currentTimeString());

                } else

                    Toast.makeText(MainActivity.this, "请检查sdCard和" + FILENAME + "文件", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void init() throws IOException {

        textView1 = (TextView)findViewById(R.id.textView1);

        textView2 = (TextView)findViewById(R.id.textView2);

        editText = (EditText)findViewById(R.id.editText);

        button = (Button)findViewById(R.id.button);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        editText.setText("1500");

        setTimes(editText.getText().toString());

        logger.debug("InitWithUserInterface");
    }

    public static Boolean isExternalWritabel(){

        String status = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(status)){
            return true;

        }else {
            return false;
        }
    }

    public static Boolean isTestFileInSDCard(String name){

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),name);

        if (!file.exists()) {

            return false;

        }else {

            return true;
        }
    }

    public File getTestFileInSDcard(String name) {

            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();

            File file  = new File(baseDir,name);

            return file;
    }

    private void logging(){

        Logging logging = new Logging();

        logging.configLog(currentTimeString(),"Logging" );

        logger = logging.getgLogger();

        logger.debug("Logging Start...");
    }


    public int getTimes(){
        return times;
    }

    public void setTimes(String str){

        int i = 0;

        if (str!= null) {
             i = Integer.valueOf(str);

            if (i > 0){

                this.times = i;
            }
        }
    }


//    public  void copyFileToInternalStorage() throws IOException {
//
//        BufferedInputStream bufferedInputStream = null;
//        BufferedOutputStream bufferedOutputStream = null;
//
//
//        FileInputStream fileInputStream = new FileInputStream(new File(testFile.getPath()));
//
//        FileOutputStream fileOutputStream = openFileOutput("succeed.rar", MODE_ENABLE_WRITE_AHEAD_LOGGING);
//
////            FileOutputStream fileOutputStream = new FileOutputStream(getFilesDir() + File.separator + "Succeed.rar");
//
//        textView1.setText(getFilesDir().getPath());
//
//        bufferedInputStream = new BufferedInputStream((fileInputStream), 1024 * 10);
//
//        bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024 * 10);
//
//        byte[] byteBuff = new byte[1024 * 10];
//
//        int bytesRead = 0;
//
//        while ((bytesRead = fileInputStream.read(byteBuff)) > 0) {
//
//            fileOutputStream.write(byteBuff, 0, bytesRead);
//
////                textView1.setText(fileInputStream.available());
//        }
//
//        bufferedOutputStream.flush();
//        bufferedInputStream.close();
//        bufferedOutputStream.close();
//
//    }


    private String currentTimeString(){

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd-HHmmss");

        Date currentDate = new Date();

        String formatDate = simpleDateFormat.format(currentDate);

        return formatDate;

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
