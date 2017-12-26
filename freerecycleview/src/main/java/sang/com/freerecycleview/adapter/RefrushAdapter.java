package sang.com.freerecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sang.com.freerecycleview.holder.BaseHolder;
import sang.com.freerecycleview.holder.FootRefrushHolder;
import sang.com.freerecycleview.holder.PeakHolder;
import sang.com.freerecycleview.holder.TopRefrushHolder;


/**
 * 作者： ${PING} on 2017/9/4.
 */

public abstract class RefrushAdapter<T> extends BaseAdapter<T> {


    public RefrushAdapter(Context context, List<T> list) {
        super(context, list);
        View view = new View(context);
        topRefrush=new TopRefrushHolder(context);
        footRefrush=new TopRefrushHolder(context);
    }

    protected final int TOPREFRUSH = 200001;//顶部刷新
    protected final int FOOTREFRUSH = 200002;//底部刷新
    protected int topRefrushPositin;//头部刷新位置
    protected TopRefrushHolder topRefrush;//头部刷新
    protected TopRefrushHolder footRefrush;//底部刷新

    @Override
    public int getItemViewType(int position) {
        if (topRefrush != null && topRefrushPositin == position) {//顶部刷新
            return TOPREFRUSH;
        } else if (footRefrush != null && position == getItemCount() - 1) {//底部刷新
            return FOOTREFRUSH;
        } else {
            if (topRefrush != null && position > topRefrushPositin) {//有顶部刷新,则在刷新位置之后,在原有基础上,position减去顶部刷新带来的影响
                position -= 1;
            }
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if (topRefrush != null) {
            itemCount++;
        }
        if (footRefrush != null) {
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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == TOPREFRUSH) {
            topRefrush.initView(position);
        } else if (viewType == FOOTREFRUSH) {
            footRefrush.initView(position);
        } else {
            if (position>topRefrushPositin){
                position-=1;
            }
            super.onBindViewHolder(holder, position);
        }

    }


}
