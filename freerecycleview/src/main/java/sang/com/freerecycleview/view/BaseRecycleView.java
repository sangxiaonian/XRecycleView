package sang.com.freerecycleview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import sang.com.freerecycleview.utils.FRLog;
import sang.com.freerecycleview.utils.FRToast;


/**
 * 作者： ${PING} on 2017/11/21.
 */

public class BaseRecycleView extends RecyclerView {


    private int heardHeight;
    protected int ORIENTATION = -1;//滑动方向 1 y方向 0位x
    protected boolean TOP; //滑动到顶部
    protected boolean BOOTOM;//滑动到底
    private long currentTimeMillis;
    private float speed;//速度

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

    private void initView(Context context) {
        setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        FRLog.d(getOverScrollMode() + "-----------onScrollStateChanged-----------" + state);



        if (ORIENTATION == 1) {
            TOP = !canScrollVertically(-1);
            BOOTOM = !canScrollVertically(1) && !TOP;
        } else {
            TOP = !canScrollHorizontally(-1);
            BOOTOM = !canScrollHorizontally(1) && !TOP;
        }


        if (TOP && speed < 0) {
            FRToast.showTextToast(getContext(), "到顶了");
            FRLog.i("到顶了:>>" + state);
        }

        if (BOOTOM && speed > 0) {
            FRToast.showTextToast(getContext(), "到底部了");
            FRLog.i("到底部了:>>" + state);
        }

        if (state == SCROLL_STATE_IDLE) {
            currentTimeMillis = 0;
            currentY = 0;
        }

    }

    private float currentY;

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (currentTimeMillis == 0) {
            currentTimeMillis = System.currentTimeMillis();
            currentY = computeVerticalScrollOffset();
        } else {
            long timegap = System.currentTimeMillis() - currentTimeMillis;
            if (timegap >= 100) {
                FRLog.i(computeHorizontalScrollOffset()+">>>"+currentY);
                speed = (computeHorizontalScrollOffset() - currentY) / timegap;
                currentTimeMillis = System.currentTimeMillis();
                currentY = computeVerticalScrollOffset();
                FRLog.e(speed + "");
            }
        }

    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

    }

    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (downY == 0) {
            downY = e.getY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (canDrag()) {
                    float v = e.getY() - downY;

                }
                downY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
                downY = 0;
                break;

        }


        return super.onTouchEvent(e);

    }

    private boolean canDrag() {
        if (currentY == 0) {
            return true;
        }

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
