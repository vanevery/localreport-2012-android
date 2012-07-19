package com.mobvcasting.localreport2012;

import java.io.File;
import android.os.Bundle;
import android.widget.TextView;

public class AudioUploader extends VideoUploader {
        File audioFile;
        String title = "An Audio Clip";
        String postingResult = "";
        long fileLength = 0;
        TextView textview;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_video_uploader);

                textview = (TextView) findViewById(R.id.textview);

                //audioFile = new File(Environment.getExternalStorageDirectory() + "/VID_20120711_144557.mp4");
                //fileLength = audioFile.length();
                UploaderTask aut = new UploaderTask();
                aut.execute();
                }
}
