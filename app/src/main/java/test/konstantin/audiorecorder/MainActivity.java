package test.konstantin.audiorecorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final String TAG = "myLogs";

    int myBufferSize = 8192;
    AudioRecord audioRecord;
    AudioTrack audioPlayer;
    boolean isReading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        createAudioRecorder();
        //createPlayer();
        Log.d(TAG, "init state = " + audioRecord.getState());
    }

    void createPlayer() {
        audioPlayer = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, myBufferSize, AudioTrack.MODE_STREAM);
    }

    void createAudioRecorder() {
        int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;
        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + myBufferSize);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
        
    }

    public void recordStart(View v) {
        Log.d(TAG, "record start");
        audioRecord.startRecording();
        int recordingState = audioRecord.getRecordingState();
        Log.d(TAG, "recordingState = " + recordingState);
    }

    public void recordStop(View v) {
        Log.d(TAG, "record stop");
        audioRecord.stop();
    }

    public void readStart(View v) {
        Log.d(TAG, "read start");
        isReading = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (audioRecord == null)
                    return;

                byte[] myBuffer = new byte[myBufferSize];
                int readCount = 0;
                int totalCount = 0;

                readCount = audioRecord.read(myBuffer, 0, myBufferSize);

                createPlayer();

                 //audioPlayer.write(myBuffer,0,readCount);
                // audioPlayer.play();


               // while (isReading) {
                   // readCount = audioRecord.read(myBuffer, 0, myBufferSize);
                  //  totalCount += readCount;
                  //  Log.d(TAG, "readCount = " + readCount + ", totalCount = "
                           // + totalCount);
                    //createPlayer();

                   // audioPlayer.write(myBuffer,0,readCount);
                   // audioPlayer.play();
                //}
            }
        }).start();
    }

    public void readStop(View v) {
        Log.d(TAG, "read stop");
        isReading = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isReading = false;
        if (audioRecord != null) {
            audioRecord.release();
        }
    }
}
