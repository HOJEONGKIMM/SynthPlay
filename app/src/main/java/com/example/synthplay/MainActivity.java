package com.example.synthplay;

import android.media.SoundPool;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int soundC, soundCSharp, soundD, soundDSharp, soundE, soundF, soundFSharp, soundG, soundGSharp, soundA, soundASharp, soundB, soundCHigh;

    private StaffCanvas staffCanvas1;
    private StaffCanvas staffCanvas2;
    private StaffCanvas staffCanvas3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // SoundPool 초기화
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();

        // 사운드 로드
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

        // 이벤트 연결
        findViewById(R.id.note_c).setOnClickListener(v -> playSound(soundC));
        findViewById(R.id.note_c_sharp).setOnClickListener(v -> playSound(soundCSharp));
        findViewById(R.id.note_d).setOnClickListener(v -> playSound(soundD));
        findViewById(R.id.note_d_sharp).setOnClickListener(v -> playSound(soundDSharp));
        findViewById(R.id.note_e).setOnClickListener(v -> playSound(soundE));
        findViewById(R.id.note_f).setOnClickListener(v -> playSound(soundF));
        findViewById(R.id.note_f_sharp).setOnClickListener(v -> playSound(soundFSharp));
        findViewById(R.id.note_g).setOnClickListener(v -> playSound(soundG));
        findViewById(R.id.note_g_sharp).setOnClickListener(v -> playSound(soundGSharp));
        findViewById(R.id.note_a).setOnClickListener(v -> playSound(soundA));
        findViewById(R.id.note_a_sharp).setOnClickListener(v -> playSound(soundASharp));
        findViewById(R.id.note_b).setOnClickListener(v -> playSound(soundB));
        findViewById(R.id.note_c_high).setOnClickListener(v -> playSound(soundCHigh));

        // 오선지 그림판 초기화
        staffCanvas1 = findViewById(R.id.drawing_canvas1);
        staffCanvas2 = findViewById(R.id.drawing_canvas2);
        staffCanvas3 = findViewById(R.id.drawing_canvas3);

        // 이퀄라이저 설정 버튼 동작
        Button equalizerButton = findViewById(R.id.equalizer_button);
        equalizerButton.setOnClickListener(v -> {
            // TODO: 이퀄라이저 화면 이동 구현 예정
        });

        // 녹음 화면 버튼을 지우기 버튼으로 사용
        Button recorderButton = findViewById(R.id.recorder_button);
        recorderButton.setText("지우기");
        recorderButton.setOnClickListener(v -> {
            // 모든 캔버스를 초기화
            if (staffCanvas1 != null) staffCanvas1.clearCanvas();
            if (staffCanvas2 != null) staffCanvas2.clearCanvas();
            if (staffCanvas3 != null) staffCanvas3.clearCanvas();
        });
    }

    private void playSound(int soundId) {
        soundPool.play(soundId, 1, 1, 0, 0, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}