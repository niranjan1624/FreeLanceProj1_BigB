package in.movies.niranjan.com.freelance1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by Niranjan on 1/5/2016.
 */
public class PhotoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEBUG_PHOTO", "photo taken");

        String selectedImageUri = intent.getData().getPath();
        Log.d("TAG", "Received new photo:::" + selectedImageUri);
        Log.d("TAG", "file Path" + getRealPathFromURI(intent.getData(), context));
    }

    public String getRealPathFromURI(Uri contentUri, Context context) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }
}