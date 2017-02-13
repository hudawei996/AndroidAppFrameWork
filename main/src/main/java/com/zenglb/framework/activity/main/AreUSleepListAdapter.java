package com.zenglb.framework.activity.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zenglb.framework.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by zenglb on 2016/10/25.
 */
public class AreUSleepListAdapter extends RecyclerView.Adapter<AreUSleepListAdapter.ViewHolder> {
	private int checkedIndex = -1;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<AreuSleepBean> data = new ArrayList<>();

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	/**
	 * @param mContext
	 * @param data
	 */
	public AreUSleepListAdapter(Context mContext, List<AreuSleepBean> data) {
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.data = data;
	}

	//====================================RecyclerView 更新封装 开始=================================================

	/**
	 * 删除一条数据
	 *
	 * @param position
	 */
	public void remove(int position) {
		data.remove(position);
		notifyItemRemoved(position);
	}

	/**
	 * 删除所有数据
	 */
	public void removeAllItems() {
		data.clear();
		notifyItemMoved(0, data.size() - 1);
	}

	//==================================== RecyclerView 更新封装完毕 =================================================
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mLayoutInflater.inflate(R.layout.areusleep_list_item, parent, false));
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
		AreuSleepBean bean = data.get(position);
		viewHolder.itemView.setClickable(true);

		viewHolder.topic.setText(bean.getTopic().trim());
		viewHolder.time.setText("发题时间 " + bean.getStart_time());

		if (mOnItemClickListener != null) {
			viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					checkedIndex = viewHolder.getLayoutPosition();
					mOnItemClickListener.onItemClick(viewHolder.itemView, checkedIndex);
				}
			});

			viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					mOnItemClickListener.onItemLongClick(viewHolder.itemView, viewHolder.getLayoutPosition());
					return true;
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		private View itemView;
		private TextView topic;
		private TextView time;

		public ViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			topic = (TextView) itemView.findViewById(R.id.topic);
			time = (TextView) itemView.findViewById(R.id.time);

		}
	}
}