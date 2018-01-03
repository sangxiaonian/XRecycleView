package sang.com.freerecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sang.com.freerecycleview.holder.BaseHolder;
import sang.com.freerecycleview.holder.FootRefrushHolder;
import sang.com.freerecycleview.holder.PeakHolder;
import sang.com.freerecycleview.holder.TopRefrushHolder;
import sang.com.freerecycleview.utils.FRLog;


/**
 * 作者： ${PING} on 2017/9/4.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    public Context context;
    protected List<T> list;
    protected List<PeakHolder> heads;
    protected List<PeakHolder> foots;

    protected final int HEADTYPE = 100000;
    protected final int FOOTTYPE = 200000;


    protected final int TOPREFRUSH = 300001;//顶部刷新
    protected final int FOOTREFRUSH = 400002;//底部刷新
    protected int topRefrushPositin = -1;//头部刷新位置
    protected TopRefrushHolder topRefrush;//头部刷新
    protected FootRefrushHolder footRefrush;//底部刷新


    public BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        heads = new ArrayList<>();
        foots = new ArrayList<>();
    }

    public int getTopRefrushPositin() {
        return topRefrushPositin>heads.size()+list.size()?heads.size()+list.size():topRefrushPositin;
    }

    public void setTopRefrushPositin(int topRefrushPositin) {
        this.topRefrushPositin = topRefrushPositin;
    }

    public TopRefrushHolder getTopRefrush() {
        return topRefrush;
    }

    public void setTopRefrush(TopRefrushHolder topRefrush) {
        this.topRefrush = topRefrush;
    }

    public FootRefrushHolder getFootRefrush() {
        return footRefrush;
    }

    public void setFootRefrush(FootRefrushHolder footRefrush) {
        this.footRefrush = footRefrush;
    }

    public void addHeard(PeakHolder heardHolder) {
        heads.add(heardHolder);
    }

    public void removeHeard(PeakHolder heardHolder) {
        heads.remove(heardHolder);
    }

    public void removeHeard(int index) {
        heads.remove(index);
    }

    public void addFoot(PeakHolder heardHolder) {
        foots.add(heardHolder);
    }

    public void addFoot(int index, PeakHolder heardHolder) {
        foots.add(index, heardHolder);
    }

    public List<PeakHolder> getFoots() {
        return foots;
    }

    public void removeFoot(int index) {
        foots.remove(index);
    }

    public void removeFoot(PeakHolder heardHolder) {

        if (foots.contains(heardHolder)) {
            foots.remove(heardHolder);
            notifyDataSetChanged();
        }
    }


    public void notifyItemAdd(int position) {
        position += heads.size();
        notifyItemInserted(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void notifyItemDeleted(int position) {
        position += heads.size();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }


    @Override
    public int getItemViewType(int position) {

        if (hasTopRefrush() && getTopRefrushPositin() == position) {//顶部刷新
            return TOPREFRUSH;
        } else if (hasFootRefrush() && position == getItemCount() - 1) {//底部刷新
            return FOOTREFRUSH;
        } else {
            if (hasTopRefrush() && position > getTopRefrushPositin()) {//有顶部刷新,则在刷新位置之后,在原有基础上,position减去顶部刷新带来的影响
                position -= 1;
            }
            if (position < heads.size()) {
                return position + HEADTYPE;
            } else if (position >= heads.size() + list.size()) {
                return FOOTTYPE + position - heads.size() - list.size();
            } else {
                position -= heads.size();
            }
        }
        return getViewType(position);
    }

    protected boolean hasFootRefrush() {
        return footRefrush != null;
    }

    protected boolean hasTopRefrush() {
        return topRefrush != null &&  getTopRefrushPositin() >= 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == TOPREFRUSH) {
            return topRefrush;
        } else if (viewType == FOOTREFRUSH) {
            return footRefrush;
        } else if (viewType >= HEADTYPE && viewType < FOOTTYPE) {
            return heads.get(viewType - HEADTYPE);
        } else if (viewType >= FOOTTYPE) {
            return foots.get(viewType - FOOTTYPE);
        } else {

            return initHolder(parent, viewType);
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
            if (hasTopRefrush() && position > getTopRefrushPositin()) {
                position--;
            }
            if (viewType >= HEADTYPE) {//脚布局
                PeakHolder holder1 = (PeakHolder) holder;
                holder1.initView(viewType - HEADTYPE);
            } else if (viewType >= FOOTTYPE) {//头布局
                PeakHolder holder1 = (PeakHolder) holder;
                holder1.initView(viewType - FOOTTYPE);
            } else {//一般布局
                position -= heads.size();
                BaseHolder holder1 = (BaseHolder) holder;
                holder1.initView(holder1.getItemView(), position, list.get(position));
            }
        }

    }


    @Override
    public int getItemCount() {

        int itemCount = list.size() + heads.size() + foots.size();
        if (hasTopRefrush()) {
            itemCount++;
        }
        if (hasFootRefrush()) {
            itemCount++;
        }
        return itemCount;
    }

    /**
     * 初始化ViewHolder,{@link BaseAdapter#onCreateViewHolder(ViewGroup, int)}处,用于在非头布局\脚布局\刷新时候
     * 调用
     *
     * @param parent   父View,即为RecycleView
     * @param viewType holder类型,在{@link BaseAdapter#getItemViewType(int)}处使用
     * @return BaseHolder或者其父类
     */
    protected abstract BaseHolder initHolder(ViewGroup parent, final int viewType);

    /**
     * 初始化XAdapter 的viewType,且此处已经经过处理,去除Header等的影响,可以直接从0开始使用
     *
     * @param position 当前item的Position(从0开始)
     * @return
     */
    public int getViewType(int position) {
        return 0;
    }


    public T getItemData(int position) {
        return list.get(position);
    }


    public List<PeakHolder> getHeads() {
        return heads;
    }
}
