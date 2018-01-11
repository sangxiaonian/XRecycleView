package sang.com.xrecycleview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import sang.com.freerecycleview.utils.FRLog;

import static android.R.attr.orientation;

/**
 * 作者： ${PING} on 2018/1/10.
 */

public class GalleyLayoutManager extends LinearLayoutManager {

    private RecyclerView recycleView;

    public GalleyLayoutManager(Context context) {
        super(context);
    }


    public GalleyLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public GalleyLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (getItemCount() <= 0 || state.isPreLayout()) { return;}

        detachAndScrapAttachedViews(recycler);//清除所有的view

        int size =200 ;
        int itemCount = state.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            View view =  recycler.getViewForPosition(i);
            addView(view);
            layoutDecoratedWithMargins(view,size*i,size*i,size*i+size,size*i+size);
            
        }





    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        this.recycleView=view;
    }
}
