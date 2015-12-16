package com.jinwang.umthink.swipemenu;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinwang.umthink.swipemenu.SwipeMenuListView.OnSwipeMenuItemClickListener;
import com.jinwang.umthink.swipemenu.SwipeMenuView.OnSwipeItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

import three.com.phoneservice.R;


/**
 * 
 * @author baoyz
 * @date 2014-8-24
 * 
 */
public class SwipeMenuAdapter extends BaseExpandableListAdapter implements OnSwipeItemClickListener {
//public class SwipeMenuAdapter implements WrapperListAdapter,OnSwipeItemClickListener {
//public class SwipeMenuAdapter implements ExpandableListAdapter,OnSwipeItemClickListener {
		
//	private ExpandableListAdapter mAdapter;
	//private MainActivityListViewAdapter mAdapter;
	private Context mContext;
	private OnSwipeMenuItemClickListener onMenuItemClickListener;
	// ������ݵ�list
	private ArrayList<HashMap<String, Object>> list;
		
	private ArrayList<ArrayList<HashMap<String, Object>>> childList;
	
	public SwipeMenuAdapter(Context context, ArrayList<HashMap<String, Object>> list,ArrayList<ArrayList<HashMap<String, Object>>> childList) {
//	public SwipeMenuAdapter(Context context, ListAdapter adapter) {
		this.list=list;
		this.childList = childList;
		mContext = context;
	}
	
	@Override
	public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
		if (onMenuItemClickListener != null) {
			onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,index);
		}
	}
	
	public void setOnMenuItemClickListener(
			OnSwipeMenuItemClickListener onMenuItemClickListener) {
		this.onMenuItemClickListener = onMenuItemClickListener;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		SwipeMenuLayout layout = null;
		ViewHolder holder = null;
		if (convertView == null) {
			// ���ViewHolder����
			holder = new ViewHolder();
						// ���벼�ֲ���ֵ��convertview
			convertView = LayoutInflater.from(mContext).inflate(R.layout.main_lamp, null);
			holder.lamp_mainLayout=(RelativeLayout)convertView.findViewById(R.id.lamp_mainLayout);
			holder.lamp_headPhoto=(ImageView)convertView.findViewById(R.id.lamp_headPhoto);
			holder.lamp_name = (TextView) convertView.findViewById(R.id.lamp_name);
			holder.lamp_data = (TextView) convertView.findViewById(R.id.lamp_data);
			holder.lamp_content = (TextView) convertView.findViewById(R.id.lamp_content);
			// Ϊview���ñ�ǩ
			convertView.setTag(holder);
			SwipeMenu menu = new SwipeMenu(mContext);
			menu.setViewType(((BaseExpandableListAdapter) this).getChildType(groupPosition, childPosition));
			createMenu(menu);
			SwipeMenuView menuView = new SwipeMenuView(menu,
					(SwipeMenuListView) parent);
			menuView.setOnSwipeItemClickListener(this);
			SwipeMenuListView listView = (SwipeMenuListView) parent;
			layout = new SwipeMenuLayout(convertView, menuView,
					listView.getCloseInterpolator(),
					listView.getOpenInterpolator());
			layout.setPosition(childPosition);
		} else {
			holder = (ViewHolder) convertView.getTag();
			layout = (SwipeMenuLayout) convertView;
			layout.closeMenu();
			layout.setPosition(childPosition);
//			View view = mAdapter.getView(position, layout.getContentView(),parent);
			//View view = mAdapter.getChildView(groupPosition, childPosition, isLastChild, layout.getContentView(), parent);
		}
		// ����list��TextView����ʾ
		if(holder!=null)
		{
			holder.lamp_headPhoto.setImageResource(Integer.parseInt(childList.get(groupPosition).get(childPosition).get("lamp_headPhoto").toString()));
			holder.lamp_name.setText(childList.get(groupPosition).get(childPosition).get("lamp_name").toString());
			holder.lamp_data.setText(childList.get(groupPosition).get(childPosition).get("lamp_date").toString());
			holder.lamp_content.setText(childList.get(groupPosition).get(childPosition).get("lamp_content").toString());
			holder.lamp_mainLayout.setBackgroundColor(0xffffffff);
		}
		return layout;
	}
	class ViewHolder {
		TextView txName;
		TextView txnumber;
		RelativeLayout lamp_mainLayout;
		ImageView lamp_headPhoto;
		TextView lamp_name;
		TextView lamp_data;
		TextView lamp_content;
	}
	
	public void createMenu(SwipeMenu menu) {
		// Test Code
		SwipeMenuItem item = new SwipeMenuItem(mContext);
		item.setTitle("Item 1");
		item.setBackground(new ColorDrawable(Color.GRAY));
		item.setWidth(300);
		menu.addMenuItem(item);

		item = new SwipeMenuItem(mContext);
		item.setTitle("Item 2");
		item.setBackground(new ColorDrawable(Color.RED));
		item.setWidth(300);
		menu.addMenuItem(item);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).size();
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item, null);
				holder = new ViewHolder();
				holder.txName = (TextView) convertView
						.findViewById(R.id.txName);
				holder.txnumber = (TextView) convertView
						.findViewById(R.id.txNumber);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			System.out.println("���ֵ�����"+list.get(groupPosition).get("groupName").toString());
			holder.txName.setText(list.get(groupPosition).get("groupName").toString());
			holder.txName.setTextSize(20);
			holder.txName.setPadding(20, 10, 0, 10);
			return convertView;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}

	

}
