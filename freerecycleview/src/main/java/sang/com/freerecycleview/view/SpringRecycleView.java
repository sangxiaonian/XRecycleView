package sang.com.freerecycleview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import sang.com.freerecycleview.config.FRConfig;
import sang.com.freerecycleview.utils.DeviceUtils;
import sang.com.freerecycleview.utils.FRLog;

import static android.R.attr.translationY;
import static android.R.attr.y;


/**
 * 作者： ${PING} on 2017/11/21.
 * 带有回弹效果的RecyclView
 */

public class SpringRecycleView extends BaseRecycleView {

    private static float standatHeitht;
    Scroller mScroller;

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
        mScroller=new Scroller(context,new DecelerateInterpolator());
    }

    /**
     * 惯性滑动到底部
     *
     * @param speed
     */
    @Override
    protected void flingScrollToBootom(float speed) {

    }

    /**
     * 惯性滑动到顶部
     *
     * @param speed
     */
    @Override
    protected void flingScrollToTop(float speed) {

    }

    /**
     * 取消拖拽
     */
    @Override
    protected void onCancleDrag() {
        animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    /**
     * 开始拖拽
     */
    @Override
    protected void onStarteDrag() {
        animate().cancel();
    }

    @Override
    protected void onDrag(float dragX, float dragY) {
        FRLog.i(dragY + "");
        if (ORIENTATION == LinearLayoutManager.VERTICAL) {
            float y = getTranslationY();
            float v = Math.abs(y / standatHeitht);
            v = v < 1 ? 1 : v;

            if (Math.abs(y)>Math.abs(y + dragY)){
                v=1;
            }

            setTranslationY(y + dragY / v);
        } else {
            float x = getTranslationX();
            float v = Math.abs(dragX / standatHeitht);
            v = v < 1 ? 1 : v;
            if (Math.abs(x)>Math.abs(x + dragY)){
                v=1;
            }
            setTranslationY(x + dragX / v);
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
        }else if (BOOTOM) {
            float translation = 0;
            if (ORIENTATION == LinearLayoutManager.VERTICAL) {//垂直方向
                translation = getTranslationY();

            } else if (ORIENTATION == LinearLayoutManager.HORIZONTAL) {
                translation = getTranslationX();
            }

            if (translation == 0) {
                candrag = y <0;
            } else if (translation < 0) {
                candrag = true;
            }
        }


        return candrag;
    }


}
