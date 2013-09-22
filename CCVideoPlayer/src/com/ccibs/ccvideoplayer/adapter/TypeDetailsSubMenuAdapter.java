package com.ccibs.ccvideoplayer.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccibs.ccvideoplayer.R;

public class TypeDetailsSubMenuAdapter extends BaseAdapter
{
  private Context context;
  private ArrayList<String>  array;
  private int selcted = -1;
  public TypeDetailsSubMenuAdapter(Context paramContext, ArrayList<String>  paramArray)
  {
    this.context = paramContext;
    if (paramArray != null)
    {
      this.array = paramArray;
      return;
    }
    this.array = new ArrayList<String>();
  }

  public int getCount()
  {
    return this.array.size();
  }

  public Object getItem(int position)
  {
    return this.array.get(position);
  }

  public long getItemId(int position)
  {
    return position;
  }

  public View getView(int position, View paramView, ViewGroup paramViewGroup)
  {
    paramView = LayoutInflater.from(this.context).inflate(R.layout.type_details_submenu, null);
    TextView localTextView = (TextView)paramView.findViewById(R.id.sunmenu_text);
//    ImageView localImageView = (ImageView)paramView.findViewById(R.id.filter_gou);
//    if (this.selcted == position)
//    {
//      localImageView.setVisibility(0);
//      paramView.setBackgroundResource(R.drawable.filter_sleted);
//    }
    localTextView.setText(array.get(position));
    return paramView;
  }

  public void setSelctItem(int paramInt)
  {
    this.selcted = paramInt;
    notifyDataSetChanged();
  }
}