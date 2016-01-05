package in.movies.niranjan.com.freelance1;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Niranjan on 1/5/2016.
 */
public class OutgoingSmsReader extends ContentObserver {

    Context mContext;
    public OutgoingSmsReader(Handler handler, Context context) {
        super(handler);
        this.mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        // save the message to the SD card here
        Uri SMS_URI = Uri.parse("content://sms/out");
        Cursor cur = mContext.getContentResolver().query(SMS_URI, null, null, null, null);
        // this will make it point to the first record, which is the last SMS sent
        cur.moveToNext();
        String content = cur.getString(cur.getColumnIndex("body"));
        Log.d("DEBUG_SENT", content);
    }
}