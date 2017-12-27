package sang.com.freerecycleview.utils;

import android.support.animation.FloatPropertyCompat;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;
import android.view.ViewGroup;

import static android.R.attr.value;

/**
 * 作者： ${PING} on 2017/12/26.
 */

public class AnimotionUtils extends FloatPropertyCompat<View> {


    public AnimotionUtils(String name) {
        super(name);
    }

    private boolean isVertical;

    public AnimotionUtils(boolean isVertical) {
        this("height");
        this.isVertical = isVertical;
    }

    public SpringAnimation creatAnimotion(View view) {
        SpringAnimation stretchAnimation = new SpringAnimation(view, this);
        stretchAnimation.animateToFinalPosition(0);
        stretchAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        stretchAnimation.start();
        return stretchAnimation;
    }

    @Override
    public float getValue(View view) {
        //返回当前属性值

        if (isVertical) {
            return view.getMeasuredHeight();
        } else {
            return view.getMeasuredWidth();
        }
    }


    @Override
    public void setValue(View view, float value) {
        //更新要修改的动画属性
        final int currentHeight = (int) value;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            if (isVertical) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, currentHeight);
            } else {
                params = new ViewGroup.LayoutParams(currentHeight, ViewGroup.LayoutParams.MATCH_PARENT);

            }
        } else {
            if (isVertical) {
                params.height = currentHeight;
            } else {
                params.width = currentHeight;
            }
        }
        view.setLayoutParams(params);
    }
}
