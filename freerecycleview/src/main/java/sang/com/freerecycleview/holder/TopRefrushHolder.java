package sang.com.freerecycleview.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： ${PING} on 2017/8/29.
 */

public class TopRefrushHolder<T> extends PeakHolder<T> {


    public TopRefrushHolder(View itemView) {
        super(itemView);
    }

    public TopRefrushHolder(Context context) {
        this(new View(context));
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params==null){
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        }else {
            params.height=0;
        }
        itemView.setLayoutParams(params);

    }




}
