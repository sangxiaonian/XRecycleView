package sang.com.freerecycleview.holder;

import android.view.View;

import sang.com.freerecycleview.view.refrush.BaseView;

/**
 * 作者： ${PING} on 2017/8/29.
 */

public class FootRefrushHolder<T> extends PeakHolder<T> {


    public FootRefrushHolder(View itemView) {
        super(itemView);

        if (itemView instanceof BaseView) {
            (( BaseView)itemView).setIsTop(false);
        }

    }
}
