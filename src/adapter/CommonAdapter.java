package adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<E> extends BaseAdapter implements
		OnClickCallback {
	private static String tag = CommonAdapter.class.getSimpleName();// 获取tag,通常是当前类的名字
	private List<E> data = new ArrayList<E>();// /**********************************
	private Context context;
	private LayoutInflater inflater;
	private int layoutId;// 需要填充的布局id

	public CommonAdapter(List<E> data, Context context, int layoutId) {
		if (data != null) {
			this.data.addAll(data);
		}
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public E getItem(int position) {
		if (position > -1 && position < getCount()) {
			return data.get(position);
		}
		Log.e(tag, "error>>>>> position is incorrect");
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseViewHolder holder;
		if (convertView != null) {
			holder = (BaseViewHolder) convertView.getTag();
			// convertView = holder.getConvertView();

		} else {
			holder = new BaseViewHolder(parent, context, layoutId, this);
			setListener(holder);
		}
		// 设置数据
		holder.setPosition(position);

		System.out.println("hint>> " + position);
		setData(holder, getItem(position));
		return holder.getConvertView();
	}

	public void append(List<E> more) {
		if (more != null && !more.isEmpty()) {
			data.addAll(more);
			notifyDataSetChanged();
		}
	}

	public void querryAppend(List<E> more) {
		data.clear();// 先对之前的数据进行清除。。。。。否则当查询到条数据的时候，界面会保持上一次的结果！！
		data.addAll(more);
		notifyDataSetChanged();
		
		
//		if (more != null && !more.isEmpty()) {//去掉判断条件，这样才能在查询到0条数据的时候也完成刷新
//			data.addAll(more);
//			notifyDataSetChanged();
//		}
	}

	public void update(Object object) {// 专门用于删除笔记后来跟新界面的方法
		data.remove(object);
		notifyDataSetChanged();
	}
	

	@Override
	public void onClick(View v, int position, BaseViewHolder viewHolder) {
		onClickCallback(v, position, viewHolder);
	}

	@Override
	public boolean onLongClick(View v, int position, BaseViewHolder viewHolder) {
		return onLonClickCallback(v, position, viewHolder);
	}

	// 设置监听
	public abstract void setListener(BaseViewHolder holder);

	// 展示数据
	public abstract void setData(BaseViewHolder holder, E item);

	// 短按回调
	public abstract void onClickCallback(View v, int position,
			BaseViewHolder viewHolder);

	// 长按回调
	public abstract boolean onLonClickCallback(View v, int position,
			BaseViewHolder viewHolder);

}
