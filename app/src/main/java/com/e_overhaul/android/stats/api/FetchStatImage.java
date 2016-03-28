package com.e_overhaul.android.stats.api;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.e_overhaul.android.stats.R;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dcox on 3/1/16.
 */
public class FetchStatImage extends AsyncTask<String, Void, Bitmap>{

    private Context _mContext;
    private static final String LOG_TAG = FetchStatImage.class.getSimpleName();
    private ImageView _mImageView;
    private String _mStat;
    private int _timeParam = 60;
    private static Map<String, String> _imageFilePaths;
    private static String _localFolder;
    final String HEIGHT_PARAM = "h";
    final String WIDTH_PARAM = "w";
    final String STAT_PARAM = "stat";
    final String TIME_PARAM = "daysBack";

    public FetchStatImage (ImageView imageView, Context context) {
        _mContext = context;
        _mImageView = imageView;
        if(_localFolder == null) {
            setLocalImagesFolder();
        }

    }
    public void setDaysBack(int daysBack) {
        _timeParam = daysBack;
    }

    public static void setLocalImagesFolder() {
        _localFolder = Environment.getExternalStorageDirectory() + "/stats_images/";
        File f = new File(_localFolder);
        if(!f.exists()) {
            try {
                f.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File[] fileList = f.listFiles();
        for(int i=0; i<fileList.length; i++) {
            Log.v(LOG_TAG, "About to delete " + fileList[i].getName());
//            fileList[i].delete();
        }

    }


    private String getStatFileName(String stat) {
        StringBuilder sb = new StringBuilder(stat.replaceAll(" ", "_"));
        sb.append(".png");
        return sb.toString();
    }

    @Override
    protected  Bitmap doInBackground(String... params) {
        Bitmap returnBitmap = null;
        _mStat = params[0];
        if(_imageFilePaths == null) {
            _imageFilePaths = new HashMap<String, String>();
            Log.v(LOG_TAG, "Creating initial _imageFilePaths");
        }
        if(_imageFilePaths.containsKey(_mStat)) {
            Bitmap bm = loadImageFromDisk(_mStat);
            return bm;
        }

        try {
            String imageURI = _mContext.getString(R.string.api_image_uri);
            // Construct the URL for the stats query
            Uri builtUri = Uri.parse(imageURI).buildUpon()
                    .appendQueryParameter(STAT_PARAM, _mStat)
                    .appendQueryParameter(WIDTH_PARAM, "1200")
                    .appendQueryParameter(HEIGHT_PARAM, "600")
                    .build();
            Log.v(LOG_TAG, "Trying to get image now");
            Log.v(LOG_TAG, builtUri.toString());
            HttpURLConnection conn = (HttpURLConnection) new java.net.URL(builtUri.toString()).openConnection();
            InputStream in = conn.getInputStream();
            if(in != null) {
                returnBitmap = BitmapFactory.decodeStream(in);
                if(returnBitmap != null) {
                    saveImageToDisk(_mStat, returnBitmap);
                } else {
                    Log.e(LOG_TAG, "Crap, can't create bitmap " + builtUri.toString());
                }
            } else {
                Log.e(LOG_TAG, "Crap, can't open image at " + builtUri.toString());
            }
            return returnBitmap;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally{
            Log.v(LOG_TAG, "in finally");
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(_mImageView != null) {
            _mImageView.setImageBitmap(result);
        }
        super.onPostExecute(result);
    }

    private void saveImageToDisk(String statName, Bitmap bitmapImage) {
        try {
            Log.v(LOG_TAG, "Saving " + statName + " to disk!");
            File file = new File(_localFolder + getStatFileName(statName));
            if( file.exists()) {
                Log.v(LOG_TAG, "Awesome, " + statName + " is already on disk");
                _imageFilePaths.put(statName, "true");
                return;
            }
            OutputStream outStream = null;
            outStream = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 85, outStream);
            outStream.flush();
            outStream.close();
            _imageFilePaths.put(statName, "true");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    Bitmap loadImageFromDisk(String statName) {
        Log.v(LOG_TAG, "In loadImageFromDisk");
        Bitmap result = null;
        InputStream in = null;
        String fileName = _localFolder + getStatFileName(statName);
        try {
            Log.v(LOG_TAG, "Attempting to load " + fileName);
            in = new FileInputStream(fileName);
            result = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
