package sang.com.freerecycleview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import sang.com.freerecycleview.R;
import sang.com.freerecycleview.holder.FootRefrushHolder;
import sang.com.freerecycleview.holder.TopRefrushHolder;
import sang.com.freerecycleview.view.refrush.DefaultRefrushView;


/**
 * 作者： ${PING} on 2017/9/4.
 */

public abstract class RefrushAdapter<T> extends BaseAdapter<T> {


    public RefrushAdapter(Context context, List<T> list) {
        super(context, list);
        topRefrush = new TopRefrushHolder(getDefaultRefrushView(context));
        footRefrush = new FootRefrushHolder(getDefaultRefrushView(context));
        topRefrushPositin = 2;
    }


    private DefaultRefrushView getDefaultRefrushView(Context context) {
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
}
