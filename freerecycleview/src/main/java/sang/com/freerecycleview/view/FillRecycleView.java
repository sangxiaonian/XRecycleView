package sang.com.freerecycleview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sang.com.freerecycleview.adapter.XAdapter;
import sang.com.freerecycleview.holder.PeakHolder;
import sang.com.freerecycleview.utils.FRLog;


/**
 * 作者： ${PING} on 2017/11/21.
 */

public class FillRecycleView extends RecyclerView {


    private float scrollY;
    private int heardHeight;
    private View itemView;

    public FillRecycleView(Context context) {
        super(context);
        initView(context);
    }


    public FillRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FillRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
//        JLog.d(getOverScrollMode() + "-----------onScrollStateChanged-----------" + state);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        scrollY += dy;
//        JLog.i(" computeVerticalScrollRange:" + computeVerticalScrollRange() +
//                "  computeVerticalScrollOffset:" + computeVerticalScrollOffset() +
//                "  computeVerticalScrollExtent:" + computeVerticalScrollExtent() +
//                ">>>" + scrollY);
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof XAdapter) {
            List<PeakHolder> heads = ((XAdapter) adapter).getHeads();
            if (heads != null && !heads.isEmpty()) {
                itemView = heads.get(0).getItemView();
                itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        heardHeight = itemView.getHeight();
                    }
                }, 20);
            }
        }
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
                    ViewGroup.LayoutParams params = itemView.getLayoutParams();
                    float v = e.getY() - downY;
                    FRLog.i(v + "");
                    params.height += (v) / 3;
                    itemView.setLayoutParams(params);
                }
                downY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
                downY = 0;
                ViewGroup.LayoutParams params = itemView.getLayoutParams();
                params.height = heardHeight;
                itemView.setLayoutParams(params);
                break;

        }


        return super.onTouchEvent(e);

    }

    private boolean canDrag() {
        if (scrollY == 0) {
            return true;
        }

        return false;
    }


}
