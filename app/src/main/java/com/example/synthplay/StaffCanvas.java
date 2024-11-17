package com.example.synthplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class StaffCanvas extends View {
    private Paint paint;
    private Path path;

    public StaffCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Paint 초기화
        paint = new Paint();
        paint.setColor(Color.BLACK); // 검은색 오선지
        paint.setStrokeWidth(5); // 선 두께
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); // 부드러운 선 처리

        // Path 초기화 (그림을 그릴 경로)
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 오선지 그리기
        drawStaff(canvas);

        // 사용자가 그린 선 경로 그리기
        canvas.drawPath(path, paint);
    }

    private void drawStaff(Canvas canvas) {
        int startY = 50; // 첫 번째 선의 Y 좌표
        int lineSpacing = 40; // 줄 간격

        // 오선지의 5개 선 그리기
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(50, startY, getWidth() - 50, startY, paint); // 화면 너비를 기준으로 선 그리기
            startY += lineSpacing;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 경로 시작
                path.moveTo(x, y);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                // 경로 이어가기
                path.lineTo(x, y);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                // 터치가 끝날 때
                break;
        }
        return super.onTouchEvent(event);
    }

    public void clearCanvas() {
        // 경로 초기화
        path.reset();
        invalidate(); // 화면 갱신 요청
    }
}
