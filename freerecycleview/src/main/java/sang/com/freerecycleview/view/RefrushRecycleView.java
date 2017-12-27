package sang.com.freerecycleview.view;

import android.content.Context;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import sang.com.freerecycleview.adapter.RefrushAdapter;
import sang.com.freerecycleview.holder.TopRefrushHolder;
import sang.com.freerecycleview.utils.AnimotionUtils;
import sang.com.freerecycleview.utils.FRLog;


/**
 * 作者： ${PING} on 2017/11/21.
 * 带有回弹效果的RecyclView
 */

public class RefrushRecycleView extends BaseRecycleView {

    Scroller mScroller;
    private View topView;
    private View footView;
    private int topStandHeight;//顶部标准高度
    private int footStandHeight;//底部标准高度
    private SpringAnimation topFling;
    private SpringAnimation footFling;


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
        mScroller = new Scroller(context, new DecelerateInterpolator());

    }

    /**
     * 惯性滑动到底部
     *
     * @param speed
     */
    @Override
    protected void flingScrollToBootom(float speed) {
        overFling(speed);
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
        float friction = Math.abs(speed / 2000);
        friction = friction > 5 ? 5 : friction;
        int value = 1;

        if (TOP) {
            if (!topFling.isRunning()) {
                topFling.animateToFinalPosition(topStandHeight * friction * value);
            }
        } else if (BOOTOM) {
            if (!footFling.isRunning()) {
                footFling.animateToFinalPosition(footStandHeight * friction * value);
            }
        }
    }

    /**
     * 取消拖拽,手指抬起
     */
    @Override
    protected void onCancleDrag() {
        if (TOP) {
            topFling.animateToFinalPosition(0);
        } else if (BOOTOM) {
            footFling.animateToFinalPosition(0);
        }
    }

    /**
     * 开始拖拽
     */
    @Override
    protected void onStarteDrag() {

    }

    @Override
    protected void onDrag(float dragX, float dragY) {
        float current = 0;
        float drag = 0;
        float v = 1;
        if (TOP) {
            if (isVertical()) {
                current = topView.getMeasuredHeight();
                drag = dragY;
            } else {
                current = topView.getMeasuredWidth();
                drag = dragX;
            }
            v = Math.abs(current / topStandHeight);
        } else if (BOOTOM) {
            if (isVertical()) {
                current = footView.getMeasuredHeight();
                drag = -dragY;
            } else {
                current = footView.getMeasuredWidth();
                drag = -dragX;
            }
            v = Math.abs(current / footStandHeight);
        }
        v = v < 1 ? 1 : v;
        if (Math.abs(current) > Math.abs(current + drag)) {
            v = 1;
        }
        changeHeight(current + drag / v);

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

    private void changeHeight(float currentHeight) {
        View view = topView;
        if (TOP) {
            view = topView;
        } else if (BOOTOM) {
            view = footView;
        }

        if (view == null) {
            return;
        }
        changeViewHeight((int) currentHeight, view);
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
            final TopRefrushHolder footRefrush = ((RefrushAdapter) adapter).getFootRefrush();

            topView = topRefrush.getItemView();
            footView = footRefrush.getItemView();

            topView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    topView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    if (isVertical()) {
                        topStandHeight = topView.getMeasuredHeight();
                    } else {
                        topStandHeight = topView.getMeasuredWidth();
                    }
                    footStandHeight = topStandHeight;
                    changeViewHeight(0, topView);
                    changeViewHeight(0, footView);
                    topFling = new AnimotionUtils(isVertical()).creatAnimotion(topView);
                    topFling.animateToFinalPosition(0);
                    topFling.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
                    topFling.start();
                    footFling = new AnimotionUtils(isVertical()).creatAnimotion(footView);
                    footFling.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                        @Override
                        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                            scrollToPosition(adapter.getItemCount() - 1);
                        }
                    });
                    footFling.animateToFinalPosition(0);
                    footFling.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
                    footFling.start();
                }
            });

        }
        super.setAdapter(adapter);

    }

    public boolean canScrollVertically(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > topStandHeight;
        } else {
            return offset < range - topStandHeight - 1;
        }
    }


}
