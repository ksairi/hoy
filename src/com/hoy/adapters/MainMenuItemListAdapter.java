package com.hoy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hoy.R;
import com.hoy.model.MainMenuItem;

import java.util.List;

/**
 * 
 * @author LDicesaro
 *
 */
public class MainMenuItemListAdapter extends ArrayAdapter<MainMenuItem<?>> {

	private final Context context;
	private final List<MainMenuItem<?>> mainMenuItems;

	static class ViewHolder {
		public ImageView icon;
		public TextView topText;
	}

	public MainMenuItemListAdapter(Context context, List<MainMenuItem<?>> mainMenuItems) {
		super(context, R.layout.main_menu_item, mainMenuItems);
		this.context = context;
		this.mainMenuItems = mainMenuItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.main_menu_item, null);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
			viewHolder.topText = (TextView) rowView.findViewById(R.id.toptext);

			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		int imageDrawableId = -1;
		String topText = "";

		if (mainMenuItems != null && !mainMenuItems.isEmpty()) {
			imageDrawableId = mainMenuItems.get(position).getImageDrawableId();
			topText = mainMenuItems.get(position).getName();
		}

		holder.icon.setImageDrawable(context.getResources().getDrawable(imageDrawableId));
		holder.topText.setText(topText);

		return rowView;
	}
}
