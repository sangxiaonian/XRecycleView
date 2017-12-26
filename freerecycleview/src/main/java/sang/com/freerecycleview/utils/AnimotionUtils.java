package sang.com.freerecycleview.utils;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.view.View;

/**
 * 作者： ${PING} on 2017/12/26.
 */

public class AnimotionUtils {

    public static SpringAnimation creatAnimotion(View view){
       return new SpringAnimation(view, DynamicAnimation.TRANSLATION_Y);

    }


}
