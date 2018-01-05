package sang.com.freerecycleview.view.refrush;

import sang.com.freerecycleview.view.RefrushRecycleView;

/**
 * 作者： ${PING} on 2018/1/2.
 */

public interface RefrushView {
    /**
     * 开始拖拽
     */
    void startDrag();

    /**
     * 拖拽处理
     * @param dragX
     * @param dragY
     */
    void onDrag(float dragX, float dragY);

    /**
     * 手指抬起或者取消拖拽
     */
    void onCancleDrag();

    /**
     * 刷新数据成功
     */
    void refrushSuccess();

    /**
     * 刷新数据失败
     */
    void refrushfail();


    void loadNoMore();

    /**
     * 对惯性滑动的处理
     * @param speed
     */
    void overFling(float speed);


    /**
     * 获取控件当前的宽高 如果是横向,则为宽度/纵向则为高度
     * @return
     */
    float getViewSize();

    /**
     *  将RecycleView 和 刷新控件结合起来
     * @param refrushRecycleView
     */
    void attachRecycleView(RefrushRecycleView refrushRecycleView);

    void setLoadListener(RefrushControl.onLoadListener loadListener);

    /**
     * 获取刷新时候控件高度
     * @return
     */
    float getStandSize();
    /**
     *  没有更多数据时候调用
     */
    void finishLoadMore();
}
