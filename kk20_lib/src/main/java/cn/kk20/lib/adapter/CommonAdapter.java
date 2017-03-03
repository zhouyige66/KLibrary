package cn.kk20.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Description:
 * Author: kk20
 * Email: 751664206@qq.com
 * Date: 2016/9/30
 * Modify: 2017/1/16 上午11:19
 * Version: V1.0.0
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected int mItemLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context,int itemLayoutId,List<T> mDatas) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterViewHolder holder = AdapterViewHolder.get(mContext,convertView,parent,
                mItemLayoutId,position);
        convert(holder,getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(AdapterViewHolder holder,T t);
}
