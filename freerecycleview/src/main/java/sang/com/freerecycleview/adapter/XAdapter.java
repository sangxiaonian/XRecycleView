package sang.com.freerecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sang.com.freerecycleview.holder.BaseHolder;
import sang.com.freerecycleview.holder.PeakHolder;


/**
 * 作者： ${PING} on 2017/9/4.
 * 带看记录使用的ViewPager
 */

public abstract class XAdapter<T> extends RecyclerView.Adapter {

    private Context context;
    private List<T> list;
    private List<PeakHolder> heads;
    private final int HEADTYPE = 100000;

    public XAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        heads = new ArrayList<>();
    }


    public void addHeard(PeakHolder heardHolder) {
        heads.add(heardHolder);
        notifyItemInserted(heads.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < heads.size()) {
            return position + HEADTYPE;
        } else {
            position -= heads.size();
        }

        return getViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType >= HEADTYPE) {
            return heads.get(viewType - HEADTYPE);
        } else {
            return initHolder(parent, viewType);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < heads.size()) {
            PeakHolder holder1 = (PeakHolder) holder;
            holder1.initView(position);
        } else {
            position -= heads.size();
            BaseHolder holder1 = (BaseHolder) holder;
            holder1.initView(holder1.getItemView(), position, list.get(position));
        }

    }


    @Override
    public int getItemCount() {
        return list.size() + heads.size();
    }

    /**
     * 初始化ViewHolder,{@link XAdapter#onCreateViewHolder(ViewGroup, int)}处,用于在非头布局\脚布局\刷新时候
     * 调用
     *
     * @param parent   父View,即为RecycleView
     * @param viewType holder类型,在{@link XAdapter#getItemViewType(int)}处使用
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


    public List<PeakHolder> getHeads() {
        return heads;
    }
}
