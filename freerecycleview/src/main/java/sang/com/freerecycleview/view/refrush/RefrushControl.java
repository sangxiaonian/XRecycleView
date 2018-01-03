package sang.com.freerecycleview.view.refrush;

/**
 * 作者： ${PING} on 2018/1/3.
 */

public class RefrushControl {

    public interface RefrushListener{
        void onRefrush();

        void onLoadMore();
    }

    public interface onLoadListener{
        void onLoad();
    }

}
