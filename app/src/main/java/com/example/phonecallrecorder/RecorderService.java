package com.example.phonecallrecorder;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class RecorderService<rec> extends Service {

    MediaRecorder recorder;
    static final String TAGS=" Inside Service";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        recorder = new MediaRecorder();
        recorder.reset();
        String phoneNumber=intent.getStringExtra("number");
        Log.d(TAGS, "Phone number in service: "+phoneNumber);
        String time=new CommonMethods().getTIme();
        String path=new CommonMethods().getPath();

        String rec=path+"/"+phoneNumber+"_"+time+".mp4";
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(rec);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAGS, "onStartCommand: "+"Recording started");
        Toast.makeText(this, "recording started", Toast.LENGTH_SHORT).show();
        return null;
    }




    public int onStartCommand(Intent intent,int flags,int startId)
    {
        recorder = new MediaRecorder();
        recorder.reset();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int source = Integer.parseInt(SP.getString("RECORDER", "1"));
        switch (source) {
            case 0:
                try {
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    String manufacturer = Build.MANUFACTURER;
                    if (manufacturer.toLowerCase().contains("samsung")) {
                        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                    } else {
                        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                    }
                } catch (Exception e) {
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        String phoneNumber=intent.getStringExtra("number");

        Log.d(TAGS, "Phone number in service: "+phoneNumber);
        String time=new CommonMethods().getTIme();
        String path=new CommonMethods().getPath();
        String rec=path+"/"+phoneNumber+"_"+time+".mp4";
        File file = new File(Environment.getExternalStorageDirectory() + "/My Records/");
        try {

            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAGS, "onStartCommand: "+"Recording started");
        Toast.makeText(this, "recording started", Toast.LENGTH_SHORT).show();
        MediaRecorderReady();
        recorder.setOutputFile(rec);
        return START_NOT_STICKY;
    }




    public void onDestroy()
    {
        super.onDestroy();
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.reset();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getpath();
        Log.d(TAGS, "onDestroy: "+"Recording stopped");
        Toast.makeText(this, "recording stopped", Toast.LENGTH_SHORT).show();

    }

    final String TAGCM="Inside Service";
    Calendar cal=Calendar.getInstance();




    public String getDate()
    {
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH)+1;
        int day=cal.get(Calendar.DATE);
        String date=String.valueOf(day)+""+String.valueOf(month)+""+String.valueOf(year);

        Log.d(TAGCM, "Date "+date);
        return date;
    }




    public String getTIme()
    {
        String am_pm="";
        int sec=cal.get(Calendar.SECOND);
        int min=cal.get(Calendar.MINUTE);
        int hr=cal.get(Calendar.HOUR);
        int amPm=cal.get(Calendar.AM_PM);
        if(amPm==1)
            am_pm="PM";
        else if(amPm==0)
            am_pm="AM";
        String time=String.valueOf(hr)+":"+String.valueOf(min)+":"+String.valueOf(sec)+" "+am_pm;
        Log.d(TAGCM, "Date "+time);
        return time;
    }




    private String getpath() {
        String internalFile = getDate();
        File file = new File(Environment.getExternalStorageDirectory() + "/My Records/");
        File file1 = new File(Environment.getExternalStorageDirectory() + "/My Records/" + internalFile + "/");

        if (!file.exists()) {
            file.mkdir();
        }
        if (!file1.exists())
            file1.mkdir();
        String path = file1.getAbsolutePath();
        Toast.makeText(this,"test"+path,Toast.LENGTH_LONG).show();
        Log.d(TAGCM, "Path " + path);
        return path;
   }


    public void MediaRecorderReady() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(String.valueOf(recorder));
    }


}
