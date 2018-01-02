package sang.com.freerecycleview.utils.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

/**
 * 作者： ${PING} on 2017/12/26.
 */

public class SpringScrollAnimation extends BaseAnimation {


    private SpringAnimation fling;

    public SpringScrollAnimation(String name) {
        super(name);
    }


    public SpringScrollAnimation(boolean isVertical) {
        this("height");
        this.isVertical = isVertical;

    }

    public SpringScrollAnimation creatAnimotion(View view) {
        animation = new SpringAnimation(view, this);
        fling= (SpringAnimation) animation;
        return this;
    }


    public SpringScrollAnimation addEndListener(DynamicAnimation.OnAnimationEndListener listener) {
        fling.addEndListener(listener);
        return this;
    }


    public void start() {
        fling. getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        fling.start();
    }

    public SpringScrollAnimation addUpdateListener(DynamicAnimation.OnAnimationUpdateListener onAnimationUpdateListener) {
        fling.addUpdateListener(onAnimationUpdateListener);
        return this;
    }






    public SpringForce setDampingRatio(float dampingRatioNoBouncy) {
        return fling.getSpring().setDampingRatio(dampingRatioNoBouncy);
    }

    public void cancel() {
        fling.cancel();
    }

    /**
     * 运动到位置
     *
     * @param value 惯性滑动时候为速度,弹性滑动为具体位置
     */
    @Override
    public void moveTo(float value) {

        fling.animateToFinalPosition(value);

    }


}
