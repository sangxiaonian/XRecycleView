package sang.com.freerecycleview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import sang.com.freerecycleview.utils.FRLog;

/**
 * 作者： ${PING} on 2018/1/10.
 * <p>
 * 拥有画廊效果的RecyclView
 */

public class GalleryRecycleView extends RecyclerView {
    public GalleryRecycleView(Context context) {
        super(context);
    }

    public GalleryRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
        int firstPositon = manager.findFirstVisibleItemPosition();
        int lastPosition = manager.findLastVisibleItemPosition();

        int orientation = manager.getOrientation();

        int tranx;

        for (int i = firstPositon; i <= lastPosition; i++) {
            View view = manager.findViewByPosition(i);
            final int start;
            final int end;
            final int center;
            final float ratio;
            if (orientation == LinearLayoutManager.VERTICAL) {//垂直方向
                start = view.getTop();
                end = view.getBottom();
                center=computeVerticalScrollExtent()/2;
            } else {
                start = view.getLeft();
                end = view.getRight();
                center=computeHorizontalScrollExtent()/2;
            }
            ratio=Math.abs((start+end)/2-center*1.0f)/(2*center);

            float v1 = 0.5f;
            float v = (1 - ratio)*v1+v1;
            v=v<v1? v1 :v;
            if (v>1){
                v=1;
            }



//            if ((start+end)/2.0f-center*1.0f>0){
//                view.setTranslationX(-view.getWidth()*(1-v));
//            }else {
//                view.setTranslationX(view.getWidth()*(1-v));
//            }

            view.setPivotX(view.getWidth()/2);
            view.setPivotY(view.getHeight()/2);
//            view.setScaleX(v);
//            view.setScaleY(v);


        }

    }
}
