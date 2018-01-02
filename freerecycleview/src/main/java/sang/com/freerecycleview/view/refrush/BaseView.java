package sang.com.freerecycleview.view.refrush;

import android.content.Context;
import android.graphics.PointF;
import android.support.animation.DynamicAnimation;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import sang.com.freerecycleview.utils.animation.SpringScrollAnimation;
import sang.com.freerecycleview.utils.animation.OverScrollAnimation;

/**
 * 作者： ${PING} on 2017/12/27.
 * 默认情况下的刷新控件
 */

public class BaseView extends View {


    protected boolean isTop = true;
    protected PointF standPoint;
    private boolean isVertical;
    private SpringScrollAnimation sprinBack;//回弹动画
    private boolean flingActivate;//惯性滑动是否激活刷新功能
    private int standSize;//标准高度
    private RecyclerView parentView;
    private OverScrollAnimation fling;


    public BaseView(Context context) {
        super(context);
        initView(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        standPoint = new PointF();
        isVertical = true;
        sprinBack = new SpringScrollAnimation(isVertical).creatAnimotion(this);
        sprinBack.moveTo(0);
        sprinBack.start();
        //惯性动画
        fling = new OverScrollAnimation(isVertical).creatAnimotion(this);
        fling.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if (!isTop && parentView != null && value > 0) {
                    parentView.scrollToPosition(parentView.getAdapter().getItemCount() - 1);
                }
            }
        }).addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                if (value != 0) {
                    sprinBack.moveTo(0, 200);
                }
            }
        })
        ;
    }

    public int getStandSize() {
        return standSize;
    }

    public void setStandSize(int standSize) {
        this.standSize = standSize;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (standPoint.x == 0 && standPoint.y == 0) {
            standPoint.x = w;
            if (h == 0) {
                h = 100;
            }
            standPoint.y = h;
            if (isVertical) {
                standSize = h;
            } else {
                standSize = w;
            }
            fling.setMaxLength(standSize * 5);
            sprinBack.setMaxLength(standSize * 5);
        }
    }

    /**
     * 拖拽监听
     *
     * @param dragX X方向拖拽
     * @param dragY Y方向拖拽
     */
    public void onDrag(float dragX, float dragY) {

        float current = 0;
        float drag = 0;
        float v = 1;
        if (isTop) {
            if (isVertical) {
                current = getMeasuredHeight();
                drag = dragY;
                v = Math.abs(current / standSize);
            } else {
                current = getMeasuredWidth();
                drag = dragX;
                v = Math.abs(current / standSize);
            }
        } else {
            if (isVertical) {
                current = getMeasuredHeight();
                drag = -dragY;
                v = Math.abs(current / standSize);
            } else {
                current = getMeasuredWidth();
                drag = -dragX;
                v = Math.abs(current / standSize);
            }
        }
        v = v < 1 ? 1 : v;
        if (Math.abs(current) > Math.abs(current + drag)) {
            v = 1;
        }

        changeHeight(isVertical, (int) (current + drag / v));

    }

    private void changeHeight(boolean vertical, int currentHeight) {
        if (currentHeight < 0) {
            currentHeight = 0;
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) {
            if (vertical) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, currentHeight);
            } else {
                params = new ViewGroup.LayoutParams(currentHeight, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        } else {
            if (vertical) {
                params.height = currentHeight;
            } else {
                params.width = currentHeight;
            }
        }
        setLayoutParams(params);
    }

    /**
     * 取消动画
     */
    public void cancle() {
        sprinBack.cancel();
        fling.cancel();
    }

    /**
     * 停止拖拽,手指抬起
     */
    public void onCancleDrag() {
        sprinBack.moveTo(0);
    }

    /**
     * 开始惯性滑动
     * @param speed
     */
    public void overFling(float speed) {
        if (!sprinBack.isRunning()) {
            fling.moveTo(Math.abs(speed));
        }
    }

    public void setParentView(RecyclerView parentView) {
        this.parentView = parentView;
    }
}
