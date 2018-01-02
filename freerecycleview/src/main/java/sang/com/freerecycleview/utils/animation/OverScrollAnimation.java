package sang.com.freerecycleview.utils.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.animation.FloatPropertyCompat;
import android.support.animation.SpringAnimation;
import android.view.View;
import android.view.ViewGroup;

import sang.com.freerecycleview.view.refrush.BaseView;

/**
 * 作者： ${PING} on 2017/12/26.
 * 惯性滑动动画
 */

public class OverScrollAnimation extends BaseAnimation {

    protected FlingAnimation fling;

    public OverScrollAnimation(boolean isVertical) {
        super(isVertical);
    }

    public OverScrollAnimation(String name) {
        super(name);
    }

    public OverScrollAnimation creatAnimotion(View view) {
        animation = new FlingAnimation(view, this);
        fling= (FlingAnimation) animation;
        return this;
    }


    public OverScrollAnimation addEndListener(DynamicAnimation.OnAnimationEndListener listener) {
        fling.addEndListener(listener);
        return this;
    }

    public OverScrollAnimation addUpdateListener(DynamicAnimation.OnAnimationUpdateListener onAnimationUpdateListener) {
        fling.addUpdateListener(onAnimationUpdateListener);
        return this;
    }



    /**
     * 运动到位置
     *
     * @param value 惯性滑动时候为速度,弹性滑动为具体位置
     */
    @Override
    public void moveTo(final float value) {
        fling.setStartVelocity(value)
                .setMinValue(0) // minimum translationX property
                .setMaxValue(value+1)  // maximum translationX property
                .setFriction(5.1f)
                .start();

    }







}
