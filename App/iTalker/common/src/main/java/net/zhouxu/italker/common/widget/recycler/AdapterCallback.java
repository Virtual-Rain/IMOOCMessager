package net.zhouxu.italker.common.widget.recycler;

/**
 * Created by zx on 2018/4/20.
 */

public interface AdapterCallback<Data> {
void update(Data data,RecyclerAdapter.ViewHolder<Data> holder);
}
