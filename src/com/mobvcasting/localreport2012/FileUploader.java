package com.mobvcasting.localreport2012;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FileUploader extends Activity {
	
	public static String LOGTAG = "FILEUPLOADER";
	
	File videoFile;
	String participantId;
	String audioOrVideo = "video";

	String postingResult = "";
	long fileLength = 0;

	TextView textview;
	
	ProgressBar mProgress;
    int mProgressStatus = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_uploader);

		textview = (TextView) findViewById(R.id.textview);
		
		mProgress = (ProgressBar) findViewById(R.id.uploadProgress);
		
        // Get the extras that have been passed in
        Bundle extras = getIntent().getExtras();
        
        // Let's find out which button was pushed to get us here
        if (extras.containsKey("filePath")) {
        	String filePath = extras.getString("filePath");
        	videoFile = new File(filePath);
        	
        	Toast.makeText(this, videoFile.toString(), Toast.LENGTH_SHORT).show();
        	
        	fileLength = videoFile.length();
        	
        	if (extras.containsKey("participant_id")) {
        		participantId = extras.getString("participant_id");
        	} else {
        		participantId = getString(R.string.default_participant_id);
        	}
        	
        	if (extras.containsKey("audio_or_video")) {
        		audioOrVideo = extras.getString("audio_or_video");
        	}
	
			UploaderTask vut = new UploaderTask();
			vut.execute();
			
        } else {
        	
        	Toast.makeText(this, "Video Not Found", Toast.LENGTH_SHORT).show();
        }
	}

	class UploaderTask extends AsyncTask<Void, String, String> implements ProgressListener 
	{
		@Override
		protected String doInBackground(Void... params) {			
			String returnString = "";
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(getString(R.string.upload_url));

			ProgressMultipartEntity multipartentity = new ProgressMultipartEntity(this);

			try {
				multipartentity.addPart("file", new FileBody(videoFile));
				multipartentity.addPart("participant_id", new StringBody(participantId));
				multipartentity.addPart("audio_or_video", new StringBody(audioOrVideo));
				multipartentity.addPart("form_submitted", new StringBody("true"));

				httppost.setEntity(multipartentity);
				HttpResponse httpresponse = httpclient.execute(httppost);
				HttpEntity responseentity = httpresponse.getEntity();
				
				returnString = EntityUtils.toString(responseentity);
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return returnString;
		}

		protected void onProgressUpdate(String... textToDisplay) {
			textview.setText(textToDisplay[0]);
		}

		protected void onPostExecute(String result) {
			Toast.makeText(FileUploader.this, result, Toast.LENGTH_LONG).show();
			Log.v(LOGTAG,result);
		}

		public void transferred(long num) {
			double percent = (double) num / (double) fileLength;
			int percentInt = (int) (percent * 100);
			mProgressStatus = percentInt;
			publishProgress("" + percentInt + "% Transferred");
			mProgress.setProgress(mProgressStatus);
		}

		public void parseResult(String result) {
			publishProgress(result);
		}
	}

	class ProgressMultipartEntity extends MultipartEntity {
		ProgressListener progressListener;

		public ProgressMultipartEntity(ProgressListener pListener) {
			super();
			this.progressListener = pListener;
		}

		@Override
		public void writeTo(OutputStream outstream) throws IOException {
			super.writeTo(new ProgressOutputStream(outstream,
					this.progressListener));
		}
	}

	interface ProgressListener {
		void transferred(long num);
	}

	static class ProgressOutputStream extends FilterOutputStream {

		ProgressListener listener;
		int transferred;

		public ProgressOutputStream(final OutputStream out,
				ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}
