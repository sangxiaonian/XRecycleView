package sang.com.freerecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;


/**
 * 作者： ${PING} on 2017/9/4.
 *
 */

public abstract class XAdapter<T> extends BaseAdapter<T> {


    public XAdapter(Context context, List<T> list) {
        super(context, list);
    }


}
