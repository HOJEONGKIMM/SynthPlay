package com.example.synthplay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int soundC, soundCSharp, soundD, soundDSharp, soundE, soundF, soundFSharp, soundG, soundGSharp, soundA, soundASharp, soundB, soundCHigh;
    private boolean soundsLoaded = false; // 사운드가 모두 로드되었는지 확인
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private StaffCanvas staffCanvas1, staffCanvas2, staffCanvas3;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean permissionToRecordAccepted = false;

    private String recordedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 오디오 녹음 권한 요청
        requestAudioPermission();

        // 볼륨 확인 및 설정
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // SoundPool 초기화
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                Log.d("SoundPool", "Sound loaded successfully: Sample ID = " + sampleId);
                if (sampleId == soundCHigh) {
                    soundsLoaded = true;
                    Log.d("SoundPool", "All sounds loaded successfully");
                }
            } else {
                Log.e("SoundPool", "Failed to load sound: Sample ID = " + sampleId);
            }
        });

        // 사운드 로드
        loadSounds();

        // 이벤트 연결
        setupNoteButtons();

        // 오선지 그림판 초기화
        staffCanvas1 = findViewById(R.id.drawing_canvas1);
        staffCanvas2 = findViewById(R.id.drawing_canvas2);
        staffCanvas3 = findViewById(R.id.drawing_canvas3);

        // 지우기 버튼 설정
        Button eraseButton = findViewById(R.id.erase_button);
        eraseButton.setText("지우기");
        eraseButton.setOnClickListener(v -> clearCanvas());

        // 녹음 버튼 설정
        Button recorderButton = findViewById(R.id.recorder_button);
        recorderButton.setOnClickListener(v -> {
            if (mediaRecorder == null) {
                startRecording();
                recorderButton.setText("녹음 중지");
            } else {
                stopRecording();
                recorderButton.setText("녹음 시작");
            }
        });

        // 이퀄라이저 버튼 설정 (화면 전환)
        Button equalizerButton = findViewById(R.id.equalizer_button);
        equalizerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EqualizerActivity.class);
            startActivity(intent);
        });

        // 녹음된 파일 선택 버튼
        Button selectFileButton = findViewById(R.id.select_file_button);
        selectFileButton.setOnClickListener(v -> showRecordedFilesDialog());
    }

    private void requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            permissionToRecordAccepted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) {
            finish();
        }
    }

    private void loadSounds() {
        soundC = soundPool.load(this, R.raw.c, 1);
        soundCSharp = soundPool.load(this, R.raw.csharp, 1);
        soundD = soundPool.load(this, R.raw.d, 1);
        soundDSharp = soundPool.load(this, R.raw.dsharp, 1);
        soundE = soundPool.load(this, R.raw.e, 1);
        soundF = soundPool.load(this, R.raw.f, 1);
        soundFSharp = soundPool.load(this, R.raw.fsharp, 1);
        soundG = soundPool.load(this, R.raw.g, 1);
        soundGSharp = soundPool.load(this, R.raw.gsharp, 1);
        soundA = soundPool.load(this, R.raw.a, 1);
        soundASharp = soundPool.load(this, R.raw.asharp, 1);
        soundB = soundPool.load(this, R.raw.b, 1);
        soundCHigh = soundPool.load(this, R.raw.c, 1);
    }

    private void setupNoteButtons() {
        findViewById(R.id.note_c).setOnClickListener(v -> playSound(soundC, "C Key"));
        findViewById(R.id.note_c_sharp).setOnClickListener(v -> playSound(soundCSharp, "C# Key"));
        findViewById(R.id.note_d).setOnClickListener(v -> playSound(soundD, "D Key"));
        findViewById(R.id.note_d_sharp).setOnClickListener(v -> playSound(soundDSharp, "D# Key"));
        findViewById(R.id.note_e).setOnClickListener(v -> playSound(soundE, "E Key"));
        findViewById(R.id.note_f).setOnClickListener(v -> playSound(soundF, "F Key"));
        findViewById(R.id.note_f_sharp).setOnClickListener(v -> playSound(soundFSharp, "F# Key"));
        findViewById(R.id.note_g).setOnClickListener(v -> playSound(soundG, "G Key"));
        findViewById(R.id.note_g_sharp).setOnClickListener(v -> playSound(soundGSharp, "G# Key"));
        findViewById(R.id.note_a).setOnClickListener(v -> playSound(soundA, "A Key"));
        findViewById(R.id.note_a_sharp).setOnClickListener(v -> playSound(soundASharp, "A# Key"));
        findViewById(R.id.note_b).setOnClickListener(v -> playSound(soundB, "B Key"));
        findViewById(R.id.note_c_high).setOnClickListener(v -> playSound(soundCHigh, "High C Key"));
    }

    private void clearCanvas() {
        staffCanvas1.clearCanvas();
        staffCanvas2.clearCanvas();
        staffCanvas3.clearCanvas();
    }

    private void startRecording() {
        if (mediaRecorder == null && permissionToRecordAccepted) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            // 내부 저장소에 파일 저장
            recordedFilePath = getFilesDir().getAbsolutePath() + "/recorded_audio_" + System.currentTimeMillis() + ".mp3";
            mediaRecorder.setOutputFile(recordedFilePath);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("MediaRecorder", "녹음 실패", e);
            }
        }
    }

    private void showRecordedFilesDialog() {
        File directory = getFilesDir();
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".mp3"));

        if (files == null || files.length == 0) {
            Toast.makeText(this, "녹음된 파일이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("녹음된 파일 선택");
        builder.setItems(fileNames, (dialog, which) -> playSelectedFile(files[which]));
        builder.setNegativeButton("취소", null);
        builder.create().show();
    }


    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(this, "녹음 완료: " + recordedFilePath, Toast.LENGTH_SHORT).show();
        }
    }

    private void playSound(int soundId, String keyName) {
        if (soundPool != null && soundsLoaded) {
            soundPool.play(soundId, 1, 1, 0, 0, 1);
            Log.d("SoundPool", "Playing sound: " + keyName);
        } else {
            Log.e("SoundPool", "Sound not loaded or SoundPool not initialized");
        }
    }

    private void playSelectedFile(File file) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "재생 중: " + file.getName(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MediaPlayer", "재생 실패", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
    }
}