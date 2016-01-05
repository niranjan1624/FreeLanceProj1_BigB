package in.movies.niranjan.com.freelance1;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    TextView helloWorld;
    static String messageStr = "Initializing...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helloWorld = (TextView) findViewById(R.id.test);
        helloWorld.setText(messageStr);

        createDirectoriesIfNotExist();

        //registerOutgoingSmsObserver();

        initializeSmsRadarService();
    }

    private void initializeSmsRadarService() {
        SmsRadar.initializeSmsRadarService(this, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
                Log.d("DEBUG_SENT", sms.getMsg());
                messageStr = messageStr + "/n" + sms.getAddress()+" : OUTGOING /n" + sms.getMsg();
            }

            @Override
            public void onSmsReceived(Sms sms) {
                Log.d("DEBUG_INCOME", sms.getMsg());
                messageStr = messageStr + "/n" + sms.getAddress()+" : INCOMING /n" + sms.getMsg();
            }
        });
    }

    private void registerOutgoingSmsObserver() {
        ContentResolver observer = this.getContentResolver();
        Uri SMS_URI = Uri.parse("content://sms/sent");
        observer.registerContentObserver(SMS_URI, true,
                new OutgoingSmsReader(new Handler(), this));
    }

    public void createDirectoriesIfNotExist() {
        File sub = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "MyApps");
        if (!sub.exists())
            sub.mkdirs();
        else {
            sub = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyApps", "Audio");
            if (!sub.exists())
                sub.mkdirs();
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
