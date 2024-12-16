//package com.example.synthplay;
//
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.media.audiofx.Equalizer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.SeekBar;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.File;
//import java.io.IOException;
//
//public class EqualizerActivity extends AppCompatActivity {
//
//    private static final int PICK_AUDIO_REQUEST = 1; // 파일 선택 요청 코드
//    private MediaPlayer mediaPlayer;
//    private Equalizer equalizer;
//    private SeekBar sliderBass, sliderTreble, sliderMid, sliderHigh;
//    private Button playButton, selectSongButton, backButton;
//    private String audioFilePath;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.equalizer_main);
//
//        // 내부 저장소에 저장된 녹음 파일 경로
//        audioFilePath = getFilesDir().getAbsolutePath() + "/recorded_audio.mp3";
//
//        // MediaPlayer 초기화
//        mediaPlayer = new MediaPlayer();
//
//        // 녹음된 파일 기본 설정
//        setupRecordedFile();
//
//        // Equalizer 초기화
//        initializeEqualizer();
//
//        // 슬라이더 초기화
//        sliderBass = findViewById(R.id.slider_bass);
//        sliderTreble = findViewById(R.id.slider_treble);
//        sliderMid = findViewById(R.id.slider_mid);
//        sliderHigh = findViewById(R.id.slider_high);
//
//        // 버튼 초기화
//        playButton = findViewById(R.id.play_button); // 새로운 ID
//        selectSongButton = findViewById(R.id.select_song_button);
//
//        // 재생 버튼 설정
//                playButton.setOnClickListener(v -> {
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                        playButton.setText("재생");
//                    } else {
//                        mediaPlayer.start();
//                        playButton.setText("일시 정지");
//                    }
//                });
//
//        // 노래 선택 버튼 설정
//        selectSongButton.setOnClickListener(v -> openAudioPicker());
//
//
//        // 슬라이더 리스너 추가
//        setupSeekBarListeners();
//
//        // 프리셋 버튼 리스너 추가
//        setupPresetButtons();
//
//        // 뒤로가기 버튼 설정
//        backButton = findViewById(R.id.back_button);
//        backButton.setOnClickListener(v -> {
//            finish();                                   // 현재 Activity를 종료하고 이전 화면으로 돌아가기
//        });
//    }
//
//    private void setupRecordedFile() {
//        File audioFile = new File(audioFilePath);
//        if (audioFile.exists()) {
//            try {
//                mediaPlayer.setDataSource(audioFilePath);
//                mediaPlayer.prepare();
//                Toast.makeText(this, "녹음된 파일을 로드했습니다!", Toast.LENGTH_SHORT).show();
//            } catch (IOException e) {
//                Toast.makeText(this, "녹음 파일 로드 실패", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(this, "녹음된 파일이 없습니다.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void openAudioPicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("audio/*"); // 오디오 파일만 선택
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(Intent.createChooser(intent, "노래 선택"), PICK_AUDIO_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null) {
//            Uri audioUri = data.getData();
//            if (audioUri != null) {
//                try {
//                    mediaPlayer.reset(); // MediaPlayer를 초기화
//                    mediaPlayer.setDataSource(this, audioUri); // 선택한 오디오 파일 설정
//                    mediaPlayer.prepare(); // 준비
//                    Toast.makeText(this, "선택한 노래가 로드되었습니다!", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    Toast.makeText(this, "파일 로드 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void initializeEqualizer() {
//        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
//        equalizer.setEnabled(true);
//
//        // Equalizer의 대역 설정 정보 로그
//        int minLevel = equalizer.getBandLevelRange()[0];
//        int maxLevel = equalizer.getBandLevelRange()[1];
//        Toast.makeText(this, "Equalizer 초기화 완료 (Level: " + minLevel + " ~ " + maxLevel + ")", Toast.LENGTH_SHORT).show();
//    }
//
//    private void setupSeekBarListeners() {
//        int minLevel = equalizer.getBandLevelRange()[0]; // 최소값 (음역대 설정)
//        int maxLevel = equalizer.getBandLevelRange()[1]; // 최대값 (음역대 설정)
//
//        // Bass 슬라이더 리스너
//        sliderBass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
//                equalizer.setBandLevel((short) 0, (short) level); // 0번 밴드에 Bass 적용
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//        // Treble 슬라이더 리스너
//        sliderTreble.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
//                equalizer.setBandLevel((short) (equalizer.getNumberOfBands() - 1), (short) level); // 마지막 밴드에 Treble 적용
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//
//        // Mid, High 슬라이더 리스너는 밴드 인덱스에 맞게 추가
//        sliderMid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int midBand = equalizer.getNumberOfBands() / 2; // 중간 대역
//                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
//                equalizer.setBandLevel((short) midBand, (short) level);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });
//    }
//
//    private void setupPresetButtons() {
//        // Custom 버튼
//        findViewById(R.id.preset_custom).setOnClickListener(v -> setPreset(50, 50, 50, 50));
//        // Pop 버튼
//        findViewById(R.id.preset_pop).setOnClickListener(v -> setPreset(60, 70, 50, 40));
//        // Classic 버튼
//        findViewById(R.id.preset_classic).setOnClickListener(v -> setPreset(40, 50, 60, 70));
//    }
//
//    private void setPreset(int bass, int treble, int mid, int high) {
//        sliderBass.setProgress(bass);
//        sliderTreble.setProgress(treble);
//        sliderMid.setProgress(mid);
//        sliderHigh.setProgress(high);
//
//        Toast.makeText(this, "프리셋 적용됨!", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//        if (equalizer != null) {
//            equalizer.release();
//            equalizer = null;
//        }
//    }
//}


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

    // 파일 선택 요청 코드
    private static final int PICK_AUDIO_REQUEST = 1;

    // MediaPlayer 및 Equalizer 객체
    private MediaPlayer mediaPlayer;
    private Equalizer equalizer;

    // 슬라이더와 버튼 변수
    private SeekBar sliderBass, sliderTreble, sliderMid, sliderHigh;
    private Button playButton, selectSongButton, backButton;

    // 기본 오디오 파일 경로
    private String audioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_main);

        // 내부 저장소에 저장된 녹음 파일 경로 설정
        audioFilePath = getFilesDir().getAbsolutePath() + "/recorded_audio.mp3";

        // MediaPlayer 초기화
        mediaPlayer = new MediaPlayer();

        // 녹음된 파일 로드
        setupRecordedFile();

        // Equalizer 초기화
        initializeEqualizer();

        // 슬라이더 및 버튼 뷰 연결
        sliderBass = findViewById(R.id.slider_bass);
        sliderTreble = findViewById(R.id.slider_treble);
        sliderMid = findViewById(R.id.slider_mid);
        sliderHigh = findViewById(R.id.slider_high);

        playButton = findViewById(R.id.play_button);
        selectSongButton = findViewById(R.id.select_song_button);
        backButton = findViewById(R.id.back_button);

        // 재생 버튼 클릭 리스너 설정
        playButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); // 재생 중일 경우 일시 정지
                playButton.setText("재생");
            } else {
                mediaPlayer.start(); // 일시 정지 상태에서 재생
                playButton.setText("일시 정지");
            }
        });

        // 노래 선택 버튼 클릭 리스너 설정
        selectSongButton.setOnClickListener(v -> showRawAudioSelection());

        // 슬라이더 리스너 추가
        setupSeekBarListeners();

        // 프리셋 버튼 리스너 추가
        setupPresetButtons();

        // 뒤로가기 버튼 클릭 리스너 설정
        backButton.setOnClickListener(v -> finish()); // 현재 액티비티 종료
    }

    // 녹음된 파일을 로드하는 메서드
    private void setupRecordedFile() {
        File audioFile = new File(audioFilePath);
        if (audioFile.exists()) { // 파일이 존재하는지 확인
            try {
                mediaPlayer.setDataSource(audioFilePath); // 파일 경로 설정
                mediaPlayer.prepare(); // 준비
                Toast.makeText(this, "녹음된 파일을 로드했습니다!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "녹음 파일 로드 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "녹음된 파일이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // raw 폴더의 파일 선택 다이얼로그 표시
    private void showRawAudioSelection() {
        // raw 폴더의 파일 이름 (확장자 제외)
        String[] audioFiles = {"got_to_be_real", "plastic_love"};

        // 다이얼로그 생성 및 파일 목록 표시
        new android.app.AlertDialog.Builder(this)
                .setTitle("노래 선택") // 제목 설정
                .setItems(audioFiles, (dialog, which) -> {
                    int resourceId = getRawResourceIdByName(audioFiles[which]); // 선택한 파일의 리소스 ID 가져오기
                    if (resourceId != 0) {
                        loadRawAudio(resourceId); // 선택한 파일 로드
                    } else {
                        Toast.makeText(this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
    }

    // 파일 이름으로 raw 리소스 ID 가져오기
    private int getRawResourceIdByName(String resourceName) {
        // 리소스 이름과 타입, 패키지 이름으로 ID 검색
        return getResources().getIdentifier(resourceName, "raw", getPackageName());
    }

    // raw 리소스를 MediaPlayer에 로드하는 메서드
    private void loadRawAudio(int resourceId) {
        try {
            mediaPlayer.reset(); // MediaPlayer 초기화
            // raw 리소스의 URI를 MediaPlayer에 설정
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + resourceId));
            mediaPlayer.prepare(); // 준비
            Toast.makeText(this, "선택한 노래가 로드되었습니다!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "파일 로드 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Equalizer 초기화
    private void initializeEqualizer() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId()); // MediaPlayer의 AudioSessionId 사용
        equalizer.setEnabled(true); // Equalizer 활성화

        // Equalizer 대역 정보 로그
        int minLevel = equalizer.getBandLevelRange()[0]; // 최소 레벨
        int maxLevel = equalizer.getBandLevelRange()[1]; // 최대 레벨
        Toast.makeText(this, "Equalizer 초기화 완료 (Level: " + minLevel + " ~ " + maxLevel + ")", Toast.LENGTH_SHORT).show();
    }

    // 슬라이더 리스너 설정
    private void setupSeekBarListeners() {
        int minLevel = equalizer.getBandLevelRange()[0]; // 최소 레벨
        int maxLevel = equalizer.getBandLevelRange()[1]; // 최대 레벨

        // Bass 슬라이더
        sliderBass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
                equalizer.setBandLevel((short) 0, (short) level); // 첫 번째 밴드에 Bass 적용
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Treble 슬라이더
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

        // Mid 슬라이더
        sliderMid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int midBand = equalizer.getNumberOfBands() / 2; // 중간 대역
                int level = minLevel + (progress * (maxLevel - minLevel) / 100);
                equalizer.setBandLevel((short) midBand, (short) level); // 중간 밴드에 Mid 적용
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // 프리셋 버튼 리스너 설정
    private void setupPresetButtons() {
        findViewById(R.id.preset_custom).setOnClickListener(v -> setPreset(50, 50, 50, 50)); // Custom 프리셋
        findViewById(R.id.preset_pop).setOnClickListener(v -> setPreset(60, 70, 50, 40)); // Pop 프리셋
        findViewById(R.id.preset_classic).setOnClickListener(v -> setPreset(40, 50, 60, 70)); // Classic 프리셋
    }

    // 프리셋 설정 메서드
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
            mediaPlayer.release(); // MediaPlayer 자원 해제
            mediaPlayer = null;
        }
        if (equalizer != null) {
            equalizer.release(); // Equalizer 자원 해제
            equalizer = null;
        }
    }
}
