package com.summer.lottery;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class BaseLotteryItem {

    protected RectF rectF;

    public BaseLotteryItem() {
    }


    public RectF getRectF() {
        return rectF == null ? new RectF() : rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }


    /**
     * 绘制普通背景
     *
     * @param canvas
     */
    abstract void drawCommonBackground(Canvas canvas, Paint mPaint, Object o);

    /**
     * 绘制开始按钮
     *
     * @param canvas
     */
    abstract void drawCenter(Canvas canvas, Paint mPaint, Object o);

    /**
     * 绘制选中的item背景
     *
     * @param canvas
     */
    abstract void drawSelectBackground(Canvas canvas, Paint mPaint, Object o);

}
