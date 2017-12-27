package sang.com.freerecycleview.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import sang.com.freerecycleview.RecycleControl;
import sang.com.freerecycleview.config.FRConfig;
import sang.com.freerecycleview.utils.FRLog;

import static android.R.attr.x;


/**
 * 作者： ${PING} on 2017/11/21.
 */

public abstract class BaseRecycleView extends RecyclerView {

    private float currentY;
    private long currentTimeMillis;


    private PointF touchPoint;//手指按下位置

    protected int ORIENTATION = -1;//滑动方向 1 y方向 0位x
    protected boolean TOP; //滑动到顶部
    protected boolean BOOTOM;//滑动到底
    private float speed;//速度

    protected RecycleControl.OnFlingScrollToPeakListener flingListener;
    private int mTouchSlop;


    public BaseRecycleView(Context context) {
        super(context);
        initView(context);
    }


    public BaseRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    protected void initView(Context context) {
        setOverScrollMode(OVER_SCROLL_NEVER);
        touchPoint = new PointF();
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }


    /**
     * 添加滑动到两端时候的速度监听
     *
     * @param flingListener
     */
    public void setOnFlingScrollToPeakListener(RecycleControl.OnFlingScrollToPeakListener flingListener) {
        this.flingListener = flingListener;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //正在拖动或者惯性滑动时候,如果时间过短,没有完成测速,来计算速度
        if (speed == 0 && currentTimeMillis != 0 && state == SCROLL_STATE_IDLE) {
            speed = (getscroll() - currentY) * 1000 / (System.currentTimeMillis() - currentTimeMillis);
        }
        if (state == SCROLL_STATE_IDLE) {
            if (TOP && speed < 0) {
                if (flingListener == null||flingListener.onFlingScrollToTop(speed)) {
                    flingScrollToTop(speed);
                }
            }

            if (BOOTOM && speed > 0) {
                if (flingListener == null||flingListener.onFlingScrollToTop(speed)) {
                    flingScrollToBootom(speed);
                }
            }
        }

        if (state == SCROLL_STATE_IDLE) {
            currentTimeMillis = 0;
            currentY = 0;
            speed = 0;
        }
    }

    /**
     * 惯性滑动到底部
     * @param speed
     */
    protected abstract void flingScrollToBootom(float speed);

    /**
     * 惯性滑动到顶部
     * @param speed
     */
    protected abstract void flingScrollToTop(float speed);


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);


        if (isVertical()) {
            TOP = !canScrollVertically(-1);
            BOOTOM = !canScrollVertically(1) && !TOP;
        } else {
            TOP = !canScrollHorizontally(-1);
            BOOTOM = !canScrollHorizontally(1) && !TOP;
        }

        if (currentTimeMillis == 0) {
            currentTimeMillis = System.currentTimeMillis();
            currentY = getscroll();
        } else {
            long timegap = System.currentTimeMillis() - currentTimeMillis;
            if (timegap >= 100) {
                speed = (getscroll() - currentY) * 1000 / timegap;
                currentTimeMillis = System.currentTimeMillis();
                currentY = getscroll();

            }
        }
    }


    /**
     * 获取滑动距离
     *
     * @return
     */
    public float getscroll() {
        if (ORIENTATION == LinearLayoutManager.VERTICAL) {
            return computeVerticalScrollOffset();
        } else {
            return computeHorizontalScrollOffset();
        }
    }

    protected boolean isVertical(){
        return ORIENTATION == LinearLayoutManager.VERTICAL;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (touchPoint.x == 0 && touchPoint.y == 0) {
                touchPoint.y = e.getRawY();
                touchPoint.x = e.getRawY();
                onStarteDrag();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (TOP||BOOTOM) {
                    touchPoint.y = e.getRawY();
                    touchPoint.x = e.getRawX();
                    onStarteDrag();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = e.getRawX();
                final float y = e.getRawY();
                final float dragX = x - touchPoint.x;
                final float dragY = y - touchPoint.y;
                if (canDrag(dragX, dragY)) {
                    onDrag(dragX, dragY);
                }
                touchPoint.y = y;
                touchPoint.x = x;
                if (canDrag(dragX,dragY)&&(!BOOTOM)){
                    return true;
                }
                    break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchPoint.y = 0;
                touchPoint.x = 0;
                onCancleDrag();
                break;
        }
        return super.onTouchEvent(e);

    }

    /**
     * 取消拖拽
     */
    protected abstract void onCancleDrag();

    /**
     * 开始拖拽
     */
    protected abstract void onStarteDrag();

    /**
     * 正在拖拽
     *
     * @param dragX
     * @param dragY
     */
    protected abstract void onDrag(float dragX, float dragY);

    /**
     * 判断当前是否可以拖拽
     *
     * @param x 此时x位置move
     * @param y 此时y位置move
     * @return
     */
    protected boolean canDrag(float x, float y) {
        return false;
    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            ORIENTATION = ((LinearLayoutManager) manager).getOrientation();

        } else if (manager instanceof StaggeredGridLayoutManager) {
            ORIENTATION = ((StaggeredGridLayoutManager) manager).getOrientation();
        }


    }
}
