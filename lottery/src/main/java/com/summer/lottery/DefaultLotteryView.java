package com.summer.lottery;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DefaultLotteryView extends BaseLotteryItem {
    private static final String TAG = "DefaultLotteryView";
    private final int offset = 10;


    @Override
    void drawCommonBackground(Canvas canvas, Paint mPaint, Object o) {
        mPaint.setColor(Color.BLUE);
        rectF.left = rectF.left + offset;
        rectF.right = rectF.right - offset;
        rectF.top = rectF.top + offset;
        rectF.bottom = rectF.bottom - offset;
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
    }

    @Override
    void drawCenter(Canvas canvas, Paint mPaint,Object o) {
        mPaint.setColor(Color.GREEN);
        rectF.left = rectF.left + offset;
        rectF.right = rectF.right - offset;
        rectF.top = rectF.top + offset;
        rectF.bottom = rectF.bottom - offset;
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
    }

    @Override
    void drawSelectBackground(Canvas canvas, Paint mPaint, Object o) {
        mPaint.setColor(Color.YELLOW);
        rectF.left = rectF.left + offset;
        rectF.right = rectF.right - offset;
        rectF.top = rectF.top + offset;
        rectF.bottom = rectF.bottom - offset;
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
    }
}
