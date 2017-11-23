package sang.com.freerecycleview.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者： ${PING} on 2017/8/29.
 */

public class PeakHolder<T> extends RecyclerView.ViewHolder {


    /**
     * holder 的根View
     */
    protected View itemView;

    public PeakHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    /**
     * 获取holer 的根View
     *
     * @return
     */
    public View getItemView() {
        return itemView;
    }

    /**
     * 初始化数据
     *
     * @param position 在所有HeadeView中的位置
     */
    public void initView(int position) {

    }
}
