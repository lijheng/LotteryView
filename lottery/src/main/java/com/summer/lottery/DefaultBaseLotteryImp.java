package com.summer.lottery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class DefaultBaseLotteryImp implements IBaseLottery {
    /**
     * 绘制背景
     *
     * @param canvas
     * @param paint
     * @param rectView 整个view的矩形区域
     * @param rectF    抽奖矩形区域
     */
    @Override
    public void drawBackground(Canvas canvas, Paint paint, Rect rectView, RectF rectF) {

    }

    /**
     * 设置奖品和控件边框的距离
     *
     * @param context
     * @return
     */
    @Override
    public int getPrizePadding(Context context) {
        return 0;
    }

}
