package org.vn.custom;

import java.util.ArrayList;

import org.vn.gl.DebugLog;
import org.vn.herowar.R;
import org.vn.model.EnemyType;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EnemyShopAdapter extends ArrayAdapter<EnemyType> {

	public EnemyShopAdapter(Activity pContext, int textViewResourceId,
			ArrayList<EnemyType> enemys) {
		super(pContext, textViewResourceId, enemys);
		context = pContext;
	}

	private class ViewHolder {
		public TextView text;
	}

	private Activity context;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View gridView = convertView;
		if (gridView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			gridView = inflater.inflate(R.layout.enemy_waiting, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			gridView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) gridView.getTag();
		holder.text.setText(this.getItem(position).armyName);
		DebugLog.d("DUC", "getView " + this.getItem(position).armyName);
		return gridView;
	}

	@Override
	public void add(EnemyType object) {
		DebugLog.d("DUC", "add " + object.armyName);
		super.add(object);
	}

}
