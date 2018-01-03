package sang.com.freerecycleview.view.refrush;

import android.content.Context;
import android.graphics.PointF;
import android.support.animation.DynamicAnimation;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import sang.com.freerecycleview.utils.DeviceUtils;
import sang.com.freerecycleview.utils.animation.SpringScrollAnimation;
import sang.com.freerecycleview.utils.animation.OverScrollAnimation;
import sang.com.freerecycleview.view.RefrushRecycleView;

import static android.R.attr.y;

/**
 * 作者： ${PING} on 2017/12/27.
 * 默认情况下的刷新控件
 */

public class BaseView extends View implements RefrushView {


    protected boolean isTop = true;
    protected boolean isVertical;
    protected SpringScrollAnimation sprinBack;//回弹动画
    protected boolean flingActivate;//惯性滑动是否激活刷新功能
    protected int standSize;//标准高度
    protected RecyclerView parentView;
    protected OverScrollAnimation fling;
    protected RefrushControl.onLoadListener loadListener;

    /**
     * 设置加载监听
     *
     * @param loadListener
     */
    @Override
    public void setLoadListener(RefrushControl.onLoadListener loadListener) {
        this.loadListener = loadListener;
    }

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
        standSize = DeviceUtils.dip2px(context, 50);

        isVertical = true;
        sprinBack = new SpringScrollAnimation(isVertical).creatAnimotion(this);
        sprinBack.moveTo(0);
        sprinBack.setMaxLength(Integer.MAX_VALUE);
        sprinBack.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                if (value == 0) {
                    changeStated(DRAGREFRUSH);
                }
            }
        });
        sprinBack.start();
        //惯性动画
        fling = new OverScrollAnimation(isVertical).creatAnimotion(this);
        fling.setMaxLength(standSize * 5);
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
                    sprinBack.moveTo(getFinishValue(), 200);
                }
            }
        })
        ;
    }

    /**
     * 获取控件刷新高度
     *
     * @return
     */
    @Override
    public float getStandSize() {
        return standSize;
    }

    @Override
    public void finishLoadMore() {

        changeStated(LOADNOMORE);
    }

    public void setStandSize(int standSize) {
        this.standSize = standSize;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
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
            } else {
                current = getMeasuredWidth();
                drag = dragX;
            }
        } else {
            if (isVertical) {
                current = getMeasuredHeight();
                drag = -dragY;
            } else {
                current = getMeasuredWidth();
                drag = -dragX;
            }
        }
        v = Math.abs(current / standSize);

        v = v < 1 ? 1 : v;
        if (Math.abs(current) > Math.abs(current + drag)) {
            v = 1;
        }

        changeHeight(isVertical, (int) (current + drag / v));


        v = v < 1 ? 1 : v;
        if (Math.abs(current) > Math.abs(current + drag)) {
            v = 1;
        }
        float value = current + drag / v;
        if (!canChangeStated()) {
            value = value < standSize ? standSize : value;
        }
        changeHeight(isVertical, (int) (value));
        if (canChangeStated()) {
            if (getMeasuredHeight() < standSize && state != DRAGREFRUSH) {
                changeStated(DRAGREFRUSH);
            } else if (getMeasuredHeight() > standSize && state != UPREFRUSH) {
                changeStated(UPREFRUSH);
            }
        }

    }

    protected void changeHeight(boolean vertical, int currentHeight) {
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
     * 更改当前状态
     *
     * @param refrush
     */
    protected void changeStated(int refrush) {
        if (refrush == state || state == LOADNOMORE) {
            return;
        }
        switch (refrush) {
            case DRAGREFRUSH:
                showDrag();
                break;
            case UPREFRUSH:
                showUPRefrush();
                break;
            case REFRUSH:
                startLoad();
                break;
            case SUCCESS:
                refrushSuccess();
                break;
            case FAIL:
                refrushSuccess();
                break;
            case LOADNOMORE:
                loadNoMore();
                break;
        }
        postInvalidate();

    }

    /**
     * 取消动画
     */
    public void cancle() {
        sprinBack.cancel();
        fling.cancel();
    }

    /**
     * 开始拖拽
     */
    @Override
    public void startDrag() {
        cancle();
    }

    /**
     * 停止拖拽,手指抬起
     */
    @Override
    public void onCancleDrag() {
        sprinBack.moveTo(0);
    }

    /**
     * 开始惯性滑动
     *
     * @param speed
     */
    public void overFling(float speed) {
        if (!sprinBack.isRunning()) {
            fling.moveTo(Math.abs(speed));
        }
    }

    @Override
    public float getViewSize() {
        float translation = 0;
        if (isVertical) {//横向
            translation = getMeasuredHeight();
        } else {
            translation = getMeasuredWidth();
        }
        return translation;
    }


    public void attachRecycleView(RefrushRecycleView refrushRecycleView) {
        parentView = refrushRecycleView;
    }

    public static final int DRAGREFRUSH = 0;//下拉刷新
    public static final int UPREFRUSH = 1;//松手刷新
    public static final int REFRUSH = 2;//正在刷新
    public static final int SUCCESS = 3;//加载成功
    public static final int FAIL = 4;//加载失败
    public static final int LOADNOMORE = 5;//加载失败
    public int state;//当前加载状态


    public boolean canChangeStated() {
        if (state == DRAGREFRUSH || state == UPREFRUSH) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更改动画结束状态
     *
     * @return
     */
    public int getFinishValue() {
        if (state == DRAGREFRUSH || state == SUCCESS || state == FAIL) {
            return 0;
        } else {
            return standSize;
        }
    }

    /**
     * 刷新加载数据成功
     */
    @Override
    public void refrushSuccess() {
        state = SUCCESS;
        sprinBack.moveTo(getFinishValue());
    }

    @Override
    public void refrushfail() {
        state = FAIL;
        sprinBack.moveTo(getFinishValue());
    }

    /**
     * 没有更多数据
     */
    @Override
    public void loadNoMore() {
        state = LOADNOMORE;
        sprinBack.moveTo(getFinishValue());
    }

    /**
     * 松手刷新
     */
    public void showUPRefrush() {
        state = UPREFRUSH;
    }

    /**
     * 下拉刷新
     */
    public void showDrag() {
        state = DRAGREFRUSH;
    }

    /**
     * 正在刷新
     */
    public void startLoad() {
        state = REFRUSH;
        if (loadListener != null) {
            loadListener.onLoad();
        }
    }

}
