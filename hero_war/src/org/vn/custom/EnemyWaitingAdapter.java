package org.vn.custom;

import java.util.ArrayList;

import org.vn.herowar.R;
import org.vn.model.Enemy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EnemyWaitingAdapter extends ArrayAdapter<Enemy> {

	public EnemyWaitingAdapter(Context pContext, int textViewResourceId,
			ArrayList<Enemy> enemys) {
		super(pContext, textViewResourceId, enemys);
		mEnemys = enemys;
		context = pContext;
	}

	private Context context;
	private final ArrayList<Enemy> mEnemys;

	// public EnemyWaitingAdapter(Context context, ArrayList<Enemy> enemys) {
	// this.context = context;
	// this.mEnemys = enemys;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {
			gridView = new View(context);
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.enemy_waiting, null);
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(mEnemys.get(position).getEnemyType().armyName);
			// set image based on selected text
			// ImageView imageView = (ImageView) gridView
			// .findViewById(R.id.grid_item_image);

		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}
}
