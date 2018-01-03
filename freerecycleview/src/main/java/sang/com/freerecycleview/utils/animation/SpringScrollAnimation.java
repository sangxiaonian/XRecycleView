package sang.com.freerecycleview.utils.animation;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

/**
 * 作者： ${PING} on 2017/12/26.
 */

public class SpringScrollAnimation extends BaseAnimation {


    private SpringAnimation spring;

    public SpringScrollAnimation(String name) {
        super(name);
    }


    public SpringScrollAnimation(boolean isVertical) {
        this("height");
        this.isVertical = isVertical;

    }

    public SpringScrollAnimation creatAnimotion(View view) {
        animation = new SpringAnimation(view, this);
        spring = (SpringAnimation) animation;
        return this;
    }


    public SpringScrollAnimation addEndListener(DynamicAnimation.OnAnimationEndListener listener) {
        spring.addEndListener(listener);
        return this;
    }


    public void start() {
        spring. getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        spring.start();
    }

    public SpringScrollAnimation addUpdateListener(DynamicAnimation.OnAnimationUpdateListener onAnimationUpdateListener) {
        spring.addUpdateListener(onAnimationUpdateListener);
        return this;
    }






    public SpringForce setDampingRatio(float dampingRatioNoBouncy) {
        return spring.getSpring().setDampingRatio(dampingRatioNoBouncy);
    }

    public void cancel() {
        spring.cancel();
    }

    /**
     * 运动到位置
     *
     * @param value 惯性滑动时候为速度,弹性滑动为具体位置
     */
    @Override
    public void moveTo(float value) {
        spring.animateToFinalPosition(value);
    }


}
