package org.vn.custom;

import org.vn.herowar.R;
import org.vn.model.Board;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BoardAdapter extends BaseAdapter {
	private Context context;
	private final Board[] mobileValues;

	public BoardAdapter(Context context, Board[] mobileValues) {
		this.context = context;
		this.mobileValues = mobileValues;
	}

	private class ViewHolder {
		public TextView text;
		public ImageView per1;
		public ImageView per2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView = convertView;
		if (gridView == null) {
			gridView = new View(context);
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.item_board, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			viewHolder.per1 = (ImageView) gridView.findViewById(R.id.per1);
			viewHolder.per2 = (ImageView) gridView.findViewById(R.id.per2);
			gridView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) gridView.getTag();
		holder.text.setText("Board" + mobileValues[position].id);
		if (mobileValues[position].nPlayer > 0) {
			holder.per1.setColorFilter(Color.TRANSPARENT);
		}else{
			holder.per1.setColorFilter(Color.BLACK);
		}
		if (mobileValues[position].nPlayer > 1) {
			holder.per2.setColorFilter(Color.TRANSPARENT);
		}else{
			holder.per2.setColorFilter(Color.BLACK);
		}
		
		return gridView;
	}

	@Override
	public int getCount() {
		return mobileValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
