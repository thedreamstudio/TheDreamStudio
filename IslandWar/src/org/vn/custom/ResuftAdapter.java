package org.vn.custom;

import org.vn.herowar.R;
import org.vn.model.Result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResuftAdapter extends BaseAdapter {
	private Context context;
	private final Result pResult;

	public ResuftAdapter(Context context, Result result) {
		this.context = context;
		this.pResult = result;
	}

	private class ViewHolder {
		public ImageView per;
		public TextView text1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView = convertView;
		if (gridView == null) {
			gridView = new View(context);
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.item_resuft, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.per = (ImageView) gridView
					.findViewById(R.id.image_winner);
			viewHolder.text1 = (TextView) gridView
					.findViewById(R.id.name_player);
			gridView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) gridView.getTag();
		if (position == 0) {
			holder.text1.setText(context.getString(R.string.is_winner,
					pResult.winnerName));
			holder.per.setVisibility(View.VISIBLE);
		} else {
			holder.text1.setText(context.getString(R.string.is_loser,
					pResult.loserName));
			holder.per.setVisibility(View.INVISIBLE);
		}
		return gridView;
	}

	@Override
	public int getCount() {
		return 2;
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
