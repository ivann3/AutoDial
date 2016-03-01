package com.example.ivan.com.application;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.Int2;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Created by ivan on 15/6/16.
 */
public class TaskAsync extends AsyncTask<String,String,String> {

    private static final String SUCCEED = "Copy Succeed";

    private BufferedInputStream bufferedInputStream = null;
    private BufferedOutputStream bufferedOutputStream = null;
    private String deletedName ;
    private TextView textView2;
    private Button button;
    private File testFile;
    private  File deleteFile;
    private  Long fileLength;
    private Context context;
    private TimesObserver timesObserver = null;
    private TimesObservable timesObservable = null;

    //EditView获取的次数
    private int times;

    private static TaskAsync task;

    private static Logger logger;

    public TaskAsync(Context context,Button button,TextView textView, File testFile,String deleteFileName,int times ) {
        super();
        this.context = context;
        this.textView2 = textView;
        this.testFile = testFile;
        this.deletedName = deleteFileName;
        this.button = button;
        this.times = times ;
    }

    @Override
    protected void onPreExecute() {

        this.fileLength = testFile.length();

        deleteFile = new File(this.context.getFilesDir(),this.deletedName);

        button.setClickable(false);

        button.setText("Copying");

        logging();

        this.times = this.times - 1 ;

        task = new TaskAsync(this.context,this.button,this.textView2,this.testFile,this.deletedName,this.times);

        if (this.testFile.exists()) {

            int i = this.times + 1;
            logger.debug(String.format("Balance:%d", i));
        }



    }

    @Override
    protected String doInBackground(String... params) {

        if (this.testFile.exists()) {

            try {

                copyFileToInternalStorage();

                logger.debug("::::Copying::::");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return SUCCEED;

        } else {

            return  "Copy Failed!";
        }
    }


    @Override
    protected void onPostExecute(String s) {

        this.textView2.setText(s);

        if(!deleteFile.exists() && s==SUCCEED){

            Toast.makeText(this.context, "文件已删除", Toast.LENGTH_SHORT).show();

            logger.debug("Copying Deleted!");

            timesObservable = new TimesObservable();

            timesObserver = new TimesObserver();

            timesObservable.addObserver(timesObserver);

            timesObserver.setTaskAsync(task);

            if (this.times > 0) {

                timesObservable.setTimes(this.times);

            }

        }else Toast.makeText(this.context,"文件还存留",Toast.LENGTH_SHORT).show();

        button.setClickable(true);

        button.setText("Start");
    }


    @Override
    protected void onProgressUpdate(String... text) {

        long readLength = Long.valueOf(text[0]).longValue();

        float percentageRead = (float)readLength / fileLength * 100;

        this.textView2.setText(String.format("%.2f%%", 100 - percentageRead));
    }

    private  void copyFileToInternalStorage() throws IOException, InterruptedException {

        FileInputStream fileInputStream = new FileInputStream(new File(this.testFile.getPath()));

//            FileInputStream fileInputStream = new FileInputStream(new File("/sdcard/test.zip"));

        FileOutputStream fileOutputStream = this.context.openFileOutput(this.deletedName, this.context.MODE_PRIVATE);

//            FileOutputStream fileOutputStream = new FileOutputStream(getFilesDir() + File.separator + "Succeed.rar");

        bufferedInputStream = new BufferedInputStream((fileInputStream),1024*10);

        bufferedOutputStream = new BufferedOutputStream(fileOutputStream,1024*10);

        byte[] byteBuff = new byte[1024 * 10];

        int bytesRead = 0;

        while ( (bytesRead= fileInputStream.read(byteBuff)) >0){

            fileOutputStream.write(byteBuff, 0, bytesRead);

            publishProgress(String.valueOf(fileInputStream.available()));

        }

        bufferedOutputStream.flush();
        bufferedInputStream.close();
        bufferedOutputStream.close();

        Thread.sleep(3000);

        deleteFile(this.context,this.deletedName);

    }


    private void deleteFile(Context context,String fileName){

        File file = new File(context.getFilesDir(),fileName);

        if (file.exists()){

            file.delete();
        }
    }


    private  static void logging(){

        Logging logging = new Logging();

        logging.configLog("ActionRecord","Status:");

        logger = logging.getgLogger();
    }
}
