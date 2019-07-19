package com.summer.lottery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class LotteryView extends View {

    private static final String TAG = "LotteryView";

    private Context context;

    private Paint mPaint,mBgPaint;

    private int winPosition = 3;

    private final int[] position = new int[]{0, 3, 6, 7, 8, 5, 2, 1};

    private Rect rect;
    private RectF prizeRectF;

    private List<Object> objectList;//需要用到的属性

    private int chooseIndex;//当前选择的奖品

    private List<BaseLotteryItem> prizes;

    private Class<? extends BaseLotteryItem> mItemView = DefaultLotteryView.class;

    private IBaseLottery iBaseLottery = new DefaultBaseLotteryImp();

    private ValueAnimator valueAnimator;

    private FinishListener finishListener;

    public void setLotterItemView(Class<? extends BaseLotteryItem> itemView) {
        mItemView = itemView;
    }


    public LotteryView(Context context) {
        this(context, null);
    }


    public LotteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
        initAnimator();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new Rect();
        prizeRectF = new RectF();
        prizes = new ArrayList<>();

        initItem();

    }

    /**
     * 初始化item
     */
    private void initItem() {
        objectList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            BaseLotteryItem lotteryItem;
            Object o = new Object();
            objectList.add(o);
            try {
                Constructor constructor = mItemView.getConstructor();
                lotteryItem = (BaseLotteryItem) constructor.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                lotteryItem = new DefaultLotteryView();
            }
            prizes.add(lotteryItem);
        }
    }

    /**
     * 初始化动画
     */
    private void initAnimator() {

        valueAnimator = ValueAnimator.ofInt(0, (48 + winPosition));//从0到48默认执行6圈
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(5000);

        /**
         * 动画更新监听
         */
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (int) animation.getAnimatedValue() % 8;
                if (chooseIndex == index) return;
                chooseIndex = index;
                postInvalidate();
            }
        });

        //动画结束监听
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (finishListener != null) {
                    finishListener.finish();
                }
            }
        });
    }

    /**
     * 设置数据
     *
     * @param objects
     */
    public void setData(List<Object> objects) {
        if (objects.size() != 9) {
            throw new RuntimeException("数据源的个数必须为9个");
        }
        objectList.clear();
        objectList.addAll(objects);
    }

    public void setFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point touchPoint = new Point((int) event.getX() - getLeft(), (int) event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //判断在点击范围
                if (inOfRect(prizes.get(4).getRectF(), touchPoint) && !valueAnimator.isRunning()) {
                    valueAnimator.start();
                    return true;
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 是否在点击范围内
     *
     * @param rectF
     * @param point
     * @return
     */
    private boolean inOfRect(RectF rectF, Point point) {
        if (rectF.left <= point.x && rectF.right >= point.x && rectF.top <= point.y && rectF.bottom >= point.y) {
            return true;
        }
        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        iBaseLottery.drawBackground(canvas,mBgPaint,rect,prizeRectF);
        initSudoku();
        drawSudoku(canvas);
    }

    public void setChooseIndex(int chooseIndex) {
        this.chooseIndex = chooseIndex;
    }

    /**
     * 绘制九宫格
     */
    private void drawSudoku(Canvas canvas) {
        for (int i = 0; i < prizes.size(); i++) {
            mPaint.setStrokeWidth(1f);
            mPaint.setColor(0xffE14A34);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            //绘制开始抽奖
            if (i == 4) {
                prizes.get(i).drawCenter(canvas, mPaint, objectList.get(8));
            } else {
                if (i == position[chooseIndex]) {
                    prizes.get(i).drawSelectBackground(canvas, mPaint, objectList.get(position[chooseIndex]));
                } else {
                    prizes.get(i).drawCommonBackground(canvas, mPaint, objectList.get(position[chooseIndex]));
                }
            }
        }
    }


    /**
     * 初始化九宫格，保存每个位置的坐标
     */
    private void initSudoku() {
        float width = getWidth() / 3f;
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RectF rectF = prizes.get(index).getRectF();
                rectF.left = i * width;
                rectF.right = (i + 1) * width;
                rectF.top = j * width;
                rectF.bottom = (j + 1) * width;
                prizes.get(index).setRectF(rectF);
                index++;
            }
        }

    }


    public void update() {
        postInvalidate();
    }

    /**
     * 重新测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }


    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    private float sp2px(float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 抽奖结束监听
     */
    public interface FinishListener {

        void finish();
    }
}
