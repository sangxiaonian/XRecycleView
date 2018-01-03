package sang.com.freerecycleview.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import sang.com.freerecycleview.adapter.RefrushAdapter;
import sang.com.freerecycleview.holder.FootRefrushHolder;
import sang.com.freerecycleview.holder.TopRefrushHolder;
import sang.com.freerecycleview.view.refrush.BaseView;
import sang.com.freerecycleview.view.refrush.RefrushControl;
import sang.com.freerecycleview.view.refrush.RefrushView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * 作者： ${PING} on 2017/11/21.
 * 带有回弹效果的RecyclView
 */

public class RefrushRecycleView extends BaseRecycleView {

    private static final int REFRUSH = 1;
    private static final int LOADMORE = 2;
    private RefrushView topView;
    private RefrushView footView;
   private RefrushControl.RefrushListener listener;



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == REFRUSH) {//下拉刷新
                refrushSuccess((Boolean) msg.obj);
            } else if (msg.what == LOADMORE) {//上拉加载
                loadMore((Boolean) msg.obj);
            }

        }
    };


    public RefrushRecycleView(Context context) {
        super(context);
    }

    public RefrushRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefrushRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
    }

    /**
     * 惯性滑动到底部
     *
     * @param speed
     */
    @Override
    protected void flingScrollToBootom(float speed) {
        overFling(speed);
        scrollToPosition(getAdapter().getItemCount() - 1);
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
        if (TOP) {
            topView.overFling(speed);
        } else if (BOOTOM) {

            footView.overFling(speed);
        }
    }

    /**
     * 取消拖拽,手指抬起
     */
    @Override
    protected void onCancleDrag() {
        if (TOP) {
            topView.onCancleDrag();
        } else if (BOOTOM) {
            footView.onCancleDrag();
        }
    }

    /**
     * 开始拖拽
     */
    @Override
    protected void onStarteDrag() {

        topView.startDrag();
        footView.startDrag();
    }

    @Override
    protected void onDrag(float dragX, float dragY) {
        if (TOP) {
            topView.onDrag(dragX, dragY);
        } else if (BOOTOM) {
            footView.onDrag(dragX, dragY);
        }
    }

    @Override
    protected boolean canDrag(float x, float y) {
        boolean candrag = false;
        if (TOP) {//顶层
            float translation = topView.getViewSize();
            if (translation == 0 || translation == topView.getStandSize()) {
                candrag = y > 0;
            } else if (translation > 0) {
                candrag = true;
            }

        } else if (BOOTOM) {
            float translation = footView.getViewSize();
            if (translation == 0 || translation == footView.getStandSize()) {
                candrag = y < 0;
            } else if (translation > 0) {
                candrag = true;
            }
        }
        return candrag;
    }

    public void setListener(RefrushControl.RefrushListener listener) {
        this.listener = listener;
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        if (adapter instanceof RefrushAdapter) {
            TopRefrushHolder topRefrush = ((RefrushAdapter) adapter).getTopRefrush();
            final FootRefrushHolder footRefrush = ((RefrushAdapter) adapter).getFootRefrush();
            topView = (RefrushView) topRefrush.getItemView();
            topView.setLoadListener(new RefrushControl.onLoadListener() {
                @Override
                public void onLoad() {
                    if (listener!=null){
                        listener.onRefrush();
                    }
                }
            });
            footView = (RefrushView) footRefrush.getItemView();
            if (footView!=null){
                footView.attachRecycleView(this);
                footView.setLoadListener(new RefrushControl.onLoadListener() {
                    @Override
                    public void onLoad() {
                        if (listener!=null){
                            listener.onLoadMore();
                        }
                    }
                });
            }
        }
        super.setAdapter(adapter);

    }

    /**
     * 是否可以滑动
     * @param direction
     * @return
     */
    public boolean canScrollVertically(int direction) {
        final int offset = computeVerticalScrollOffset();
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > topView.getStandSize();
        } else {
            return offset < range - topView.getStandSize() - 1;
        }
    }


    /**
     * 是否刷新成功
     *
     * @param isSuccess true 刷新成功 false 刷新失败
     */
    public void refrushSuccess(boolean isSuccess) {
        if (topView != null) {
            if (isSuccess){
                topView.refrushSuccess();
            }else {
                topView.refrushfail();
            }
        }
    }


    /**
     * 是否刷新成功
     *
     * @param isSuccess true 刷新成功 false 刷新失败
     * @param delay     延迟时间
     */
    public void refrushSuccess(final boolean isSuccess, long delay) {
        Message obtain = Message.obtain();
        obtain.what = REFRUSH;
        obtain.obj = isSuccess;
        if (handler.hasMessages(REFRUSH)) {
            handler.removeMessages(REFRUSH);
        }

        handler.sendMessageDelayed(obtain, delay);
    }

    /**
     * 是否加载更多成功
     *
     * @param isSuccess true 刷新成功 false 刷新失败
     */
    public void loadMore(boolean isSuccess) {
        if (footView != null) {
            if (isSuccess){
                footView.refrushSuccess();
            }else {
                footView.refrushfail();
            }
        }
    }

    /**
     * 是否加载更多完成
     *
     */
    public void finishloadMore() {
        footView.loadNoMore();
    }


    /**
     * 是否加载更多
     *
     * @param isSuccess true 刷新成功 false 刷新失败
     * @param delay     延迟时间
     */
    public void loadMore(final boolean isSuccess, long delay) {

        Message obtain = Message.obtain();
        obtain.what = LOADMORE;
        obtain.obj = isSuccess;
        if (handler.hasMessages(LOADMORE)) {
            handler.removeMessages(LOADMORE);
        }
        handler.sendMessageDelayed(obtain, delay);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler.hasMessages(REFRUSH)){
            handler.removeMessages(REFRUSH);
        }
        if (handler.hasMessages(LOADMORE)){
            handler.removeMessages(LOADMORE);
        }

    }
}
