package sang.com.freerecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import sang.com.freerecycleview.R;
import sang.com.freerecycleview.holder.TopRefrushHolder;


/**
 * 作者： ${PING} on 2017/9/4.
 */

public abstract class RefrushAdapter<T> extends BaseAdapter<T> {


    public RefrushAdapter(Context context, List<T> list) {
        super(context, list);
        topRefrush = new TopRefrushHolder(context, R.layout.item_bottom);
        footRefrush = new TopRefrushHolder(context, R.layout.item_bottom);
        topRefrushPositin=0;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (hasTopRefrush()) {
            itemCount++;
        }
        if (hasFootRefrush()) {
            itemCount++;
        }
        return itemCount;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == TOPREFRUSH) {
            return topRefrush;
        } else if (viewType == FOOTREFRUSH) {
            return footRefrush;
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }





}
