package sang.com.freerecycleview;

/**
 * 作者： ${PING} on 2017/10/17.
 */

public class RecycleControl {

    /**
     * 惯性滑动到两端的监听
     */
    public interface OnFlingScrollToPeakListener{
        /**
         * 惯性滑动到顶部时候调用
         * @param speed
         */
        boolean onFlingScrollToTop(float speed);

        /**
         * 惯性滑动到底部的时候调用
         * @param speed
         */
        boolean onFlingScrollToBootom(float speed);

    }
}
