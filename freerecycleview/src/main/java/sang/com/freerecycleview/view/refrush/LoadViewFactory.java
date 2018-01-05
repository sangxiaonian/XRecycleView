package sang.com.freerecycleview.view.refrush;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： ${PING} on 2018/1/5.
 */

public class LoadViewFactory {

    public static BaseView creatView(BaseView refrushView) {
        ViewGroup.LayoutParams params = refrushView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        } else {
            params.height = 100;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        refrushView.setLayoutParams(params);
        return refrushView;
    }


    public static DefaultRefrushView getDefaultRefrushView(Context context) {
        DefaultRefrushView refrushView = new DefaultRefrushView(context);
        ViewGroup.LayoutParams params = refrushView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        } else {
            params.height = 100;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        refrushView.setLayoutParams(params);
        return refrushView;
    }

    public static DefaultLoadMoreView getDefaultLoadMore(Context context) {
        DefaultLoadMoreView refrushView = new DefaultLoadMoreView(context);
        ViewGroup.LayoutParams params = refrushView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        } else {
            params.height = 100;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        refrushView.setLayoutParams(params);
        return refrushView;
    }
}
