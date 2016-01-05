package in.movies.niranjan.com.freelance1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Niranjan on 1/4/2016.
 */
public class CallReceiver extends BroadcastReceiver {
    static int prevState = -1;
    static String prevIncomingNum = "";
    static String phoneNumber = "";
    MediaRecorder recorder = new MediaRecorder();;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == Intent.ACTION_NEW_OUTGOING_CALL) {
            Log.d("DEBUG", intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        } else {
            CallStateListener phoneListener=new CallStateListener();
            TelephonyManager telephony = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }
    public class CallStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state,String incomingNumber){
            Log.d("DEBUG_D",state + " " + prevState + " " + incomingNumber + "  "+ prevIncomingNum);
            if(prevState != state || (!prevIncomingNum.equals("") && !prevIncomingNum.equals(incomingNumber) &&
            !incomingNumber.equals(""))) {
                switch(state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("DEBUG", "IDLE");
                        try {
                            recorder.stop();
                        } catch (Exception e) {
                            recorder = null;
                            return;
                        }
                        recorder = null;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if(prevState != TelephonyManager.CALL_STATE_OFFHOOK) {
                            Log.d("DEBUG", "OFFHOOK");
                            recorder = new MediaRecorder();
                            recorder.reset();
                            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                            recorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyApps/Audio/"+
                                     phoneNumber + "_" + System.currentTimeMillis()+".mp3");
                            try {
                                recorder.prepare();
                            } catch (java.io.IOException e) {
                                Log.d("DEBUG_E", e.getMessage());
                                recorder = null;
                                return;
                            }
                            recorder.start();
                        }
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("DEBUG", "RINGING");
                        phoneNumber = incomingNumber;
                        break;
                }
            }
            prevState = state;
            prevIncomingNum = incomingNumber;
        /*
        *  MediaRecorder recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(your_desired_folder_path);
         try {
            recorder.prepare();
        } catch (java.io.IOException e) {
            recorder = null;
            return;
        }
        recorder.start();
        * */
        }
    }

}
