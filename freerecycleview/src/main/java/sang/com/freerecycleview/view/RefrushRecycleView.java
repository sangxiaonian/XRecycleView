package sang.com.freerecycleview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import sang.com.freerecycleview.adapter.RefrushAdapter;
import sang.com.freerecycleview.holder.FootRefrushHolder;
import sang.com.freerecycleview.holder.TopRefrushHolder;
import sang.com.freerecycleview.view.refrush.BaseView;


/**
 * 作者： ${PING} on 2017/11/21.
 * 带有回弹效果的RecyclView
 */

public class RefrushRecycleView extends BaseRecycleView {

    private BaseView topView;
    private BaseView footView;



    public RefrushRecycleView(Context context) {
        super(context);
    }

    public RefrushRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefrushRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
    }
    /**
     * 惯性滑动到底部
     *
     * @param speed
     */
    @Override
    protected void flingScrollToBootom(float speed) {
        overFling(speed);
        scrollToPosition(getAdapter().getItemCount()-1);
    }

    /**
     * 惯性滑动到顶部
     *
     * @param speed
     */
    @Override
    protected void flingScrollToTop(float speed) {
        overFling(speed);
    }

    private void overFling(float speed) {
        if (Math.abs(speed) < 1000) {
            return;
        }
        if (TOP) {
            topView.overFling(speed);
        } else if (BOOTOM) {

            footView.overFling(speed);
        }
    }

    /**
     * 取消拖拽,手指抬起
     */
    @Override
    protected void onCancleDrag() {
        if (TOP) {
            topView.onCancleDrag();
        } else if (BOOTOM) {
            footView.onCancleDrag();
        }
    }

    /**
     * 开始拖拽
     */
    @Override
    protected void onStarteDrag() {
        topView.cancle();
        footView.cancle();
    }

    @Override
    protected void onDrag(float dragX, float dragY) {
        if (TOP) {
            topView.onDrag(dragX, dragY);
        } else if (BOOTOM) {
            footView.onDrag(dragX, dragY);
        }
    }

    @Override
    protected boolean canDrag(float x, float y) {
        boolean candrag = false;
        if (TOP) {//顶层
            float translation = 0;
            if (isVertical()) {//垂直方向
                translation = topView.getMeasuredHeight();
            } else {
                translation = topView.getMeasuredWidth();
            }
            if (translation == 0) {
                candrag = y > 0;
            } else if (translation > 0) {
                candrag = true;
            }

        } else if (BOOTOM) {
            float translation = 0;
            if (isVertical()) {//横向
                translation = footView.getMeasuredHeight();
            } else {
                translation = footView.getMeasuredWidth();
            }
            if (translation == 0) {
                candrag = y < 0;
            } else if (translation > 0) {
                candrag = true;
            }
        }
        return candrag;
    }


    private void changeViewHeight(int currentHeight, View view) {
        if (currentHeight < 0) {
            currentHeight = 0;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            if (isVertical()) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, currentHeight);
            } else {
                params = new ViewGroup.LayoutParams(currentHeight, ViewGroup.LayoutParams.MATCH_PARENT);

            }
        } else {
            if (isVertical()) {
                params.height = currentHeight;
            } else {
                params.width = currentHeight;
            }
        }
        view.setLayoutParams(params);
    }


    @Override
    public void setAdapter(final Adapter adapter) {
        if (adapter instanceof RefrushAdapter) {
            TopRefrushHolder topRefrush = ((RefrushAdapter) adapter).getTopRefrush();
            final FootRefrushHolder footRefrush = ((RefrushAdapter) adapter).getFootRefrush();

            topView = (BaseView) topRefrush.getItemView();
            footView = (BaseView) footRefrush.getItemView();
            if (footView!=null){
                footView.setParentView(this);
            }



        }
        super.setAdapter(adapter);

    }

    public boolean canScrollVertically(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > topView.getStandSize();
        } else {
            return offset < range - topView.getStandSize() - 1;
        }
    }


}
