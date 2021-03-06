 package com.mobvcasting.localreport2012;

import java.io.File;
import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.media.MediaRecorder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AudioCapture extends Activity implements OnClickListener {

    private TextView statusTextView;
    private Button uploadButton, stopRecording;
    private MediaRecorder recorder;

    private File audioFile;
	String filePath;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      initUI();
    }

    public void initUI() {
        setContentView(R.layout.activity_audio_capture);

        statusTextView = (TextView) this.findViewById(R.id.statusText);
        statusTextView.setText("Ready...");

        stopRecording = (Button) this.findViewById(R.id.captureButton);
        uploadButton = (Button) this.findViewById(R.id.uploadButton);
        stopRecording.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        stopRecording.setEnabled(false);
        uploadButton.setEnabled(false);
		
	}
    
    private void startRecording() {
        //construct a MediaRecorder
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        //create new file where MediaRecorder will save audio
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/local-report/audio_files/");
        path.mkdirs();

        try {
                audioFile = File.createTempFile("recording", ".gp", path);
                filePath = audioFile.getAbsolutePath();
        } catch (IOException e){
                throw new RuntimeException("Failed to create recording audio file", e);
        }
        recorder.setOutputFile(audioFile.getAbsolutePath());
        /*
        //prepare and begin recording
        try {
                recorder.prepare();
        } catch (IllegalStateException e) {
                throw new RuntimeException("Illegal State Exception on MediaRecorder.prepare",e);
        } catch (IOException e) {
                throw new RuntimeException("IOException on MediaRecorder.prepare",e);
        }

        recorder.start();
        */
        //Update UI
        statusTextView.setText("Recording");
        stopRecording.setEnabled(true);
        uploadButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
            if (v == stopRecording) {
                    /*
                    //stop Recorder
                    recorder.stop();
                    recorder.release();
                    */

                    //update UI
                    statusTextView.setText("Ready to Upload");
                    uploadButton.setEnabled(true);
                    stopRecording.setEnabled(false);

            } else if (v == uploadButton) {
    			// Upload it
    			Intent fileUpIntent = new Intent(this,FileUploader.class);
    			fileUpIntent.putExtra("filePath", filePath);
    			fileUpIntent.putExtra("audio_or_video", "audio");
    			startActivity(fileUpIntent);
    			finish();
            }
    }
}

