package com.inhand.milk.fragment.temperature_amount.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inhand.milk.R;
import com.inhand.milk.fragment.temperature_amount.details.DetailsFragment.ItemEntity;
import com.inhand.milk.fragment.temperature_amount.details.PinnedHeaderListView.HeadViewManager;

import java.util.List;

public class PinnedListViewAdapter extends  BaseAdapter implements HeadViewManager{
	
	private List<ItemEntity> mData ;
	private Context mContext;
	private LayoutInflater inflater;
	private boolean isTemperature = true;
	//����ע��������Ӧ�ã���������������������޸���mdata ����Ҳ��ı䣬�������������Ҫ�Ĺ���
	public PinnedListViewAdapter(Context context , List<ItemEntity> data) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mData = data;
	    inflater  =  LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mData == null)
			return 0;
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mData == null)
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.temperature_amount_details_content, null);
			viewHolder  = new ViewHolder();
			viewHolder.title = (TextView)convertView.findViewById(R.id.details_content_header_date);
			viewHolder.titleLayout = (View)convertView.findViewById(R.id.details_content_header_layout);
			viewHolder.amount = (TextView)convertView.findViewById(R.id.num);
			viewHolder.time  = (TextView)convertView.findViewById(R.id.time);
			viewHolder.picture = (ImageView)convertView.findViewById(R.id.one);
			viewHolder.layoutLine = (View)convertView.findViewById(R.id.details_content_down_dividing_line);
			
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		//ѡ�����
		if(needtitle(position)){
			viewHolder.title.setText(  mData.get(position).getTitle());
			viewHolder.titleLayout.setVisibility(View.VISIBLE);
		}
		else 
			viewHolder.titleLayout.setVisibility(View.GONE);
		
		if (isMove(position))
			viewHolder.layoutLine.setVisibility(View.GONE);
		else 
			viewHolder.layoutLine.setVisibility(View.VISIBLE);
		//��������
		String amount = mData.get(position).getAmount();
		viewHolder.amount.setText(  amount );
		viewHolder.time.setText( mData.get(position).getTime());
		viewHolder.picture.setImageDrawable(mData.get(position).getPicture());
		return convertView;
	}
	
	
	private boolean needtitle(int position){
		if(position == 0 )
			return true;
		
		ItemEntity currentItemEntity = (ItemEntity)mData.get(position);
		ItemEntity previousItemEntity = (ItemEntity)mData.get(position - 1);
		
		if(currentItemEntity == null || previousItemEntity == null)
			return false;
		
		String currentTitle = currentItemEntity.getTitle();
		String previousTitle = previousItemEntity.getTitle();
		if(  currentTitle ==null || previousTitle == null  )
			return false;
		
		if(currentTitle.equals( previousTitle ))
			return false;
		return true;	
		
	}
	
	private boolean isMove(int position){
		if(position == 0)
			return false;
		if(position +1 > mData.size() - 1)
			return false;
		ItemEntity currentItemEntity = mData.get(position);
		ItemEntity nextItemEntity = mData.get(position + 1);
		if( currentItemEntity == null || nextItemEntity == null){
			return false;
		}
		String currentTitle = currentItemEntity.getTitle();
		String nextTitle = nextItemEntity.getTitle();
		if(currentTitle == null || nextTitle == null)
			return false;
		if(currentTitle.equals(nextTitle))
			return false;
		return true;
	}
	@Override
	public int getHeadViewState(int position) {
		// TODO Auto-generated method stub
		if (mData.size() <=0)
			return HeadViewManager.HEAD_VIEW_GONE;
		
		if ( isMove(position))
			return HeadViewManager.HEAD_VIEW_MOVE;
		
		return HeadViewManager.HEAD_VIEW_VISIBLE;
	}

	@Override
	public void configureHeadView(View view,int position) {
		// TODO Auto-generated method stub
		ItemEntity itemEntity = mData.get(position);
		String title = itemEntity.getTitle();
		if (!title.equals("") ) {
			TextView textView = (TextView)view.findViewById(R.id.details_content_header_date);
			textView.setText(title);
		}
	}
	
	private class ViewHolder {
        TextView title;
        ImageView picture;
        TextView amount;
        TextView time;
        View  titleLayout;
        View  layoutLine;
    }
	
	
}

