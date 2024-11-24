package com.example.synthplay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class EqualizerActivity extends AppCompatActivity {

    private static final int PICK_AUDIO_REQUEST = 1; // 파일 선택 요청 코드
    private MediaPlayer mediaPlayer;
    private Equalizer equalizer;
    private SeekBar sliderBass, sliderTreble, sliderMid, sliderHigh;
    private Button playButton, selectSongButton, backButton;
    private String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_main);

        // 내부 저장소에 저장된 녹음 파일 경로
        audioFilePath = getFilesDir().getAbsolutePath() + "/recorded_audio.mp3";

        // MediaPlayer 초기화
        mediaPlayer = new MediaPlayer();

        // 녹음된 파일 기본 설정
        setupRecordedFile();

        // Equalizer 초기화
        initializeEqualizer();

        // 슬라이더 초기화
        sliderBass = findViewById(R.id.slider_bass);
        sliderTreble = findViewById(R.id.slider_treble);
        sliderMid = findViewById(R.id.slider_mid);
        sliderHigh = findViewById(R.id.slider_high);

        // 버튼 초기화
        playButton = findViewById(R.id.play_button); // 새로운 ID
        selectSongButton = findViewById(R.id.select_song_button);

        // 재생 버튼 설정
                playButton.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        playButton.setText("재생");
                    } else {
                        mediaPlayer.start();
                        playButton.setText("일시 정지");
                    }
                });

        // 노래 선택 버튼 설정
        selectSongButton.setOnClickListener(v -> openAudioPicker());


        // 슬라이더 리스너 추가
        setupSeekBarListeners();

        // 프리셋 버튼 리스너 추가
        setupPresetButtons();

        // 뒤로가기 버튼 설정
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();                                   // 현재 Activity를 종료하고 이전 화면으로 돌아가기
        });
    }

    private void setupRecordedFile() {
        File audioFile = new File(audioFilePath);
        if (audioFile.exists()) {
            try {
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.prepare();
                Toast.makeText(this, "녹음된 파일을 로드했습니다!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "녹음 파일 로드 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "녹음된 파일이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*"); // 오디오 파일만 선택
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "노래 선택"), PICK_AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri audioUri = data.getData();
            if (audioUri != null) {
                try {
                    mediaPlayer.reset(); // MediaPlayer를 초기화
                    mediaPlayer.setDataSource(this, audioUri); // 선택한 오디오 파일 설정
                    mediaPlayer.prepare(); // 준비
                    Toast.makeText(this, "선택한 노래가 로드되었습니다!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "파일 로드 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private void initializeEqualizer() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        equalizer.setEnabled(true);

        // Equalizer의 대역 설정 정보 로그
        int minLevel = equalizer.getBandLevelRange()[0];
        int maxLevel = equalizer.getBandLevelRange()[1];
        Toast.makeText(this, "Equalizer 초기화 완료 (Level: " + minLevel + " ~ " + maxLevel + ")", Toast.LENGTH_SHORT).show();
    }

    private void setupSeekBarListeners() {
        int minLevel = equalizer.getBandLevelRange()[0]; // 최소값 (음역대 설정)
        int maxLevel = equalizer.getBandLevelRange()[1]; // 최대값 (음역대 설정)

        // Bass 슬라이더 리스너
        sliderBass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
                equalizer.setBandLevel((short) 0, (short) level); // 0번 밴드에 Bass 적용
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Treble 슬라이더 리스너
        sliderTreble.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
                equalizer.setBandLevel((short) (equalizer.getNumberOfBands() - 1), (short) level); // 마지막 밴드에 Treble 적용
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Mid, High 슬라이더 리스너는 밴드 인덱스에 맞게 추가
        sliderMid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int midBand = equalizer.getNumberOfBands() / 2; // 중간 대역
                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
                equalizer.setBandLevel((short) midBand, (short) level);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupPresetButtons() {
        // Custom 버튼
        findViewById(R.id.preset_custom).setOnClickListener(v -> setPreset(50, 50, 50, 50));
        // Pop 버튼
        findViewById(R.id.preset_pop).setOnClickListener(v -> setPreset(60, 70, 50, 40));
        // Classic 버튼
        findViewById(R.id.preset_classic).setOnClickListener(v -> setPreset(40, 50, 60, 70));
    }

    private void setPreset(int bass, int treble, int mid, int high) {
        sliderBass.setProgress(bass);
        sliderTreble.setProgress(treble);
        sliderMid.setProgress(mid);
        sliderHigh.setProgress(high);

        Toast.makeText(this, "프리셋 적용됨!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (equalizer != null) {
            equalizer.release();
            equalizer = null;
        }
    }
}
