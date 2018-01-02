package sang.com.freerecycleview.holder;

import android.view.View;

import sang.com.freerecycleview.view.refrush.BaseView;

/**
 * 作者： ${PING} on 2017/8/29.
 */

public class TopRefrushHolder<T> extends PeakHolder<T> {


    public TopRefrushHolder(View itemView) {
        super(itemView);
        if (itemView instanceof BaseView) {
            ((BaseView) itemView).setIsTop(true);
        }
    }


}
