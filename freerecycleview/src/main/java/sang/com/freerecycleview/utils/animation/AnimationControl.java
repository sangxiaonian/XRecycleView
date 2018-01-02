package sang.com.freerecycleview.utils.animation;

import android.support.animation.DynamicAnimation;
import android.view.View;

/**
 * 作者： ${PING} on 2017/12/28.
 */

public interface AnimationControl {

    AnimationControl creatAnimotion(View view);

    AnimationControl addEndListener(DynamicAnimation.OnAnimationEndListener listener);

    AnimationControl addUpdateListener(DynamicAnimation.OnAnimationUpdateListener onAnimationUpdateListener);

    void cancel();

    /**
     * 运动到位置
     *
     * @param value 惯性滑动时候为速度,弹性滑动为具体位置
     */
    void moveTo(float value);

    /**
     * 延迟指定时间之后运动到位置
     *
     * @param value 惯性滑动时候为速度,弹性滑动为具体位置
     */
    void moveTo(float value, long delay);

    /**
     * 动画是否正在执行
     * @return
     */
    boolean isRunning();

    /**
     * 设置动画最大距离
     * @return
     */
    void setMaxLength(int maxLength);
}
