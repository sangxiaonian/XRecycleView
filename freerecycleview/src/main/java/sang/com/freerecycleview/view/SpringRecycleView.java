package sang.com.freerecycleview.view;

import android.content.Context;
import android.media.JetPlayer;
import android.support.animation.DynamicAnimation;
import android.support.animation.FloatValueHolder;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import sang.com.freerecycleview.config.FRConfig;
import sang.com.freerecycleview.utils.DeviceUtils;
import sang.com.freerecycleview.utils.FRLog;


/**
 * 作者： ${PING} on 2017/11/21.
 * 带有回弹效果的RecyclView
 */

public class SpringRecycleView extends BaseRecycleView {

    private static float standatHeitht;
    Scroller mScroller;
    private SpringAnimation fling;


    public SpringRecycleView(Context context) {
        super(context);
    }

    public SpringRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpringRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        standatHeitht = DeviceUtils.dip2px(context, FRConfig.SPRINGHEIGHT);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        fling = new SpringAnimation(this, DynamicAnimation.TRANSLATION_Y);

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
        float friction = Math.abs(speed / 10000);
        friction = friction > 1 ? 1 : friction;
        int value = 1;
        if (speed > 0) {
            value = -1;
        }
        fling.animateToFinalPosition(standatHeitht * friction * value);
        fling.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        fling.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                FRLog.d(value+">>"+velocity);
                if (value!=0) {
                    fling.animateToFinalPosition(0);
                    fling.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
                }
            }
        });
        fling.start();
    }

    /**
     * 取消拖拽
     */
    @Override
    protected void onCancleDrag() {
        fling.animateToFinalPosition(0);
        fling.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        fling.start();
    }

    /**
     * 开始拖拽
     */
    @Override
    protected void onStarteDrag() {
        animate().cancel();
        fling.cancel();
    }

    @Override
    protected void onDrag(float dragX, float dragY) {
        if (ORIENTATION == LinearLayoutManager.VERTICAL) {
            float y = getTranslationY();
            float v = Math.abs(y *5/ standatHeitht);
            v = v < 1 ? 1 : v;
            if (Math.abs(y) > Math.abs(y + dragY)) {
                v = 1;
            }

            FRLog.d(y+">>"+dragY+">>>"+(y + dragY / v));

            setTranslationY(y + dragY / v);
        } else {
            float x = getTranslationX()*5;
            float v = Math.abs(x*5 / standatHeitht);
            v = v < 1 ? 1 : v;
            if (Math.abs(x) > Math.abs(x + dragY)) {
                v = 1;
            }
            setTranslationX(x + dragX / v);
        }
    }

    @Override
    protected boolean canDrag(float x, float y) {
        boolean candrag = false;
        if (TOP) {//顶层
            float translation = 0;
            if (ORIENTATION == LinearLayoutManager.VERTICAL) {//垂直方向
                translation = getTranslationY();

            } else if (ORIENTATION == LinearLayoutManager.HORIZONTAL) {
                translation = getTranslationX();
            }
            if (translation == 0) {
                candrag = y > 0;
            } else if (translation > 0) {
                candrag = true;
            }
        } else if (BOOTOM) {
            float translation = 0;
            if (ORIENTATION == LinearLayoutManager.VERTICAL) {//垂直方向
                translation = getTranslationY();

            } else if (ORIENTATION == LinearLayoutManager.HORIZONTAL) {
                translation = getTranslationX();
            }

            if (translation == 0) {
                candrag = y < 0;
            } else if (translation < 0) {
                candrag = true;
            }
        }


        return candrag;
    }


}
