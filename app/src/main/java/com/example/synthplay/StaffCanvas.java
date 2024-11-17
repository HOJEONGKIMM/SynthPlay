package com.example.synthplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class StaffCanvas extends View {
    private Paint linePaint; // 오선지 선을 그릴 페인트
    private Paint drawPaint; // 사용자가 그림을 그릴 페인트
    private List<Float> pointsX; // 그림의 X 좌표 저장
    private List<Float> pointsY; // 그림의 Y 좌표 저장

    public StaffCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 오선지 페인트 초기화
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);

        // 사용자 드로잉 페인트 초기화
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLUE); // 그림 색상
        drawPaint.setStrokeWidth(10); // 선 두께
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setAntiAlias(true);

        // 좌표를 저장할 리스트 초기화
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 배경색 설정
        canvas.drawColor(Color.WHITE);

        // 오선지 그리기
        drawStaff(canvas);

        // 사용자가 그린 선 그리기
        for (int i = 1; i < pointsX.size(); i++) {
            canvas.drawLine(pointsX.get(i - 1), pointsY.get(i - 1),
                    pointsX.get(i), pointsY.get(i), drawPaint);
        }
    }

    private void drawStaff(Canvas canvas) {
        int startY = 100; // 첫 번째 선의 시작 Y 좌표
        int lineSpacing = 40; // 줄 간격

        for (int i = 0; i < 5; i++) { // 오선지 5줄 그리기
            canvas.drawLine(50, startY, getWidth() - 50, startY, linePaint);
            startY += lineSpacing;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 사용자가 화면을 누르기 시작할 때
                pointsX.add(event.getX());
                pointsY.add(event.getY());
                invalidate(); // 화면 다시 그리기 요청
                return true;

            case MotionEvent.ACTION_MOVE: // 사용자가 드래그할 때
                pointsX.add(event.getX());
                pointsY.add(event.getY());
                invalidate(); // 화면 다시 그리기 요청
                return true;

            case MotionEvent.ACTION_UP: // 손을 뗐을 때
                pointsX.add(null); // 드로잉 구분을 위해 null 추가
                pointsY.add(null);
                return true;

            default:
                return false;
        }
    }

    // 캔버스를 지우는 메서드
    public void clearCanvas() {
        pointsX.clear();
        pointsY.clear();
        invalidate(); // 화면 다시 그리기 요청
    }
}
