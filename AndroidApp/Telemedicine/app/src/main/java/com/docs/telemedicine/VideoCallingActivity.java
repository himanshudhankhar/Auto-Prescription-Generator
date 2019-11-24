package com.docs.telemedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.TokenWatcher;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import java.io.File;
import java.io.IOException;

public class VideoCallingActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {

    private static String API_KEY = "46459532";
    private static String SESSION_ID = "1_MX40NjQ1OTUzMn5-MTU3NDAyMDk5ODQwMn4xY3pLTG0xcTBRZUdwWllmbisreTRyc3R-UH4";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjQ1OTUzMiZzaWc9OWFlODg5ZGQxNmNhY2I0YzVlMjdmMTU1OGY4NWZlNjc2ZTNjZDAxZDpzZXNzaW9uX2lkPTFfTVg0ME5qUTFPVFV6TW41LU1UVTNOREF5TURrNU9EUXdNbjR4WTNwTFRHMHhjVEJSWlVkd1dsbG1iaXNyZVRSeWMzUi1VSDQmY3JlYXRlX3RpbWU9MTU3NDAyMTAyNSZub25jZT0wLjQ2MDUxMjMwMTEzOTExODU2JnJvbGU9bW9kZXJhdG9yJmV4cGlyZV90aW1lPTE1NzQwNDI2MjImaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    private Session mSession;
    private FrameLayout mPublisherViewContainer;
    private FrameLayout mSubscriberViewContainer;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    WavRecorder wavRecorder;



    //It will have a Media Recorder outthere
    private static MediaRecorder mRecorder;
    File audiofile = null;
    static final String TAG = "MediaRecording";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
//            mRecorder.stop();
//            mRecorder.release();
//            //send this file to the main server
//            Toast.makeText(getApplication(),"Stopped Recording",Toast.LENGTH_LONG).show();
////            uploadFile("http://cc22f918.ngrok.io/patient_audio",audiofile);

            wavRecorder.stopRecording();
addRecordingToMediaLibrary();
            mSession.disconnect();


            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {

        super.onStop();
//        mRecorder.stop();
//        mRecorder.release();
//        //send this file to the main server
//        Toast.makeText(getApplication(),"Stopped Recording",Toast.LENGTH_LONG).show();
//        uploadFile("http://cc22f918.ngrok.io/patient_audio",audiofile);
//        mSession.disconnect();



        finish();

    }

    public void onBackPressed() {
//        mRecorder.stop();
//        mRecorder.release();
//        //send this file to the main server
//        Toast.makeText(getApplication(),"Stopped Recording",Toast.LENGTH_LONG).show();
//        uploadFile("http://cc22f918.ngrok.io/patient_audio",audiofile);
        wavRecorder.stopRecording();
        addRecordingToMediaLibrary();
        mSession.disconnect();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_calling);
        Intent intent = getIntent();
       SESSION_ID = intent.getExtras().getString("SESSION_ID");
        TOKEN = intent.getExtras().getString("TOKEN");
        mRecorder = new MediaRecorder();
        System.out.println(SESSION_ID);
        System.out.println(TOKEN);

        //start nedia recorder here
        requestPermissions();



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = { Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO ,Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(this, perms)) {
            mPublisherViewContainer = (FrameLayout)findViewById(R.id.publisher_container);
            mSubscriberViewContainer = (FrameLayout)findViewById(R.id.subscriber_container);
            mSession = new Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(this);


            File dir = Environment.getExternalStorageDirectory();
//            try {
//                audiofile = File.createTempFile("conversation", ".wav", dir);
//            } catch (IOException e) {
//                Log.e(TAG, "external storage access error",e);
//                return;
//            }
//

            String fileOutput = dir.getAbsolutePath() + "/conversation.wav";
            wavRecorder = new WavRecorder(fileOutput);
//            try {
//
//                mRecorder = new MediaRecorder();
////                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
////                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
////                mRecorder.setOutputFile(audiofile.getAbsolutePath());
////                System.out.println(audiofile.getAbsolutePath());
////                mRecorder.prepare();
////                mRecorder.start();
                wavRecorder.startRecording();
//            }catch(IOException e){
//                Log.e("MediaRecorder Prepare","may be a error while setting media recorder");
//            }
            mSession.connect(TOKEN);



        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }
    @Override
    public void onConnected(Session session) {
        Log.i(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);

        mPublisherViewContainer.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView){
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);
        //here we are going to start the audio recording

    }

    @Override
    public void onDisconnected(Session session) {
        Log.i(LOG_TAG, "Session Disconnected");
    }


    protected void addRecordingToMediaLibrary() {
        //creating content values of size 4
//        ContentValues values = new ContentValues(4);
//        long current = System.currentTimeMillis();
//        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
//        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
//        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
//        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
//
//        //creating content resolver and storing it in the external content uri
//        ContentResolver contentResolver = getContentResolver();
//        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Uri newUri = contentResolver.insert(base, values);
//
//        //sending broadcast message to scan the media file so that it can be available
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
//        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
  // start a new background service here which will send file to server
        File dir = Environment.getExternalStorageDirectory();
        File audiofile = new File(   dir.getAbsolutePath() + "/conversation.wav");

            uploadFile("http://5fc0c10b.ngrok.io/patient_audio",audiofile);

    }

    public static Boolean uploadFile(String serverURL, File file) {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("audio/wav"), file))
                    .addFormDataPart("some-field", "some-value")
                    .build();

            Request request = new Request.Builder()
                    .url(serverURL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(final Call call, final IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                   System.err.println("Cannot upload file to server");
                    }
                        System.out.println(response.body().string());
                    System.out.println("Succees file uploaded");
                }
            });

            return true;
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return false;
    }





    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Received");
        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberViewContainer.addView(mSubscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.i(LOG_TAG, "Stream Dropped");
        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberViewContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.getMessage());
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated");

    }
    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed");
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.getMessage());
    }
}
