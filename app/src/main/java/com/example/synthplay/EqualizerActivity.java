package com.example.synthplay;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EqualizerActivity extends AppCompatActivity {

    private SeekBar sliderBass, sliderTreble, sliderMid, sliderHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_main);

        // 슬라이더 초기화
        sliderBass = findViewById(R.id.slider_bass);
        sliderTreble = findViewById(R.id.slider_treble);
        sliderMid = findViewById(R.id.slider_mid);
        sliderHigh = findViewById(R.id.slider_high);

        // 슬라이더 리스너 추가
        setupSeekBarListeners();
    }

    private void setupSeekBarListeners() {
        // Bass 슬라이더 리스너
        sliderBass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(EqualizerActivity.this, "Bass: " + progress, Toast.LENGTH_SHORT).show();
                // TODO: Equalizer의 Bass 설정 값 변경 코드 추가
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 시작 시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 끝냈을 때
            }
        });

        // Treble 슬라이더 리스너
        sliderTreble.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(EqualizerActivity.this, "Treble: " + progress, Toast.LENGTH_SHORT).show();
                // TODO: Equalizer의 Treble 설정 값 변경 코드 추가
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 시작 시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 끝냈을 때
            }
        });

        // Mid 슬라이더 리스너
        sliderMid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(EqualizerActivity.this, "Mid: " + progress, Toast.LENGTH_SHORT).show();
                // TODO: Equalizer의 Mid 설정 값 변경 코드 추가
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 시작 시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 끝냈을 때
            }
        });

        // High 슬라이더 리스너
        sliderHigh.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(EqualizerActivity.this, "High: " + progress, Toast.LENGTH_SHORT).show();
                // TODO: Equalizer의 High 설정 값 변경 코드 추가
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 시작 시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 슬라이더 조작 끝냈을 때
            }
        });
    }
}