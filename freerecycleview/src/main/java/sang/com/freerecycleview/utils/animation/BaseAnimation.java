package sang.com.freerecycleview.utils.animation;

import android.os.Handler;
import android.os.Message;
import android.support.animation.DynamicAnimation;
import android.support.animation.FloatPropertyCompat;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： ${PING} on 2017/12/28.
 */

public abstract class BaseAnimation extends FloatPropertyCompat<View> implements AnimationControl {

    protected int maxLength;
    protected DynamicAnimation animation;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            moveTo((Float) msg.obj);
        }
    };


    public BaseAnimation(boolean isVertical) {
        this("height");
        this.isVertical = isVertical;
    }

    protected boolean isVertical;

    public BaseAnimation(String name) {
        super(name);

    }


    public BaseAnimation creatAnimotion(View view) {
        return null;
    }

    @Override
    public void cancel() {
        animation.cancel();
        if (handler != null) {
            handler.removeMessages(0);

        }
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
        int currentHeight = (int) value;
        if (currentHeight > maxLength) {
            currentHeight = maxLength;
            cancel();
        }
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

    private int getCurrentHeight(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int current = 0;
        if (params == null) {
            if (isVertical) {
                current = view.getMeasuredHeight();
            } else {
                current = view.getMeasuredWidth();
            }
        } else {
            if (isVertical) {
                current = params.height;
            } else {
                current = params.width;
            }
        }

        return current;
    }


    /**
     * 动画是否正在执行
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return animation.isRunning();
    }


    /**
     * 设置动画最大距离
     *
     * @return
     */
    @Override
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }


    /**
     * 运动到位置
     *
     * @param value 惯性滑动时候为速度,弹性滑动为具体位置
     * @param delay
     */
    @Override
    public void moveTo(float value, long delay) {
        Message obtain = Message.obtain();
        obtain.obj = value;
        obtain.what = 0;
        handler.sendMessageDelayed(obtain, delay);
    }
}
