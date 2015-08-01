package fluper.webradic.keepcleanf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerAdapter extends PagerAdapter{

	int[] flag;
	Context c;
	String[] data;
	LayoutInflater inflator;
	public ViewPagerAdapter(Context c,String[] data, int[] flag) {
		// TODO Auto-generated constructor stub
	this.c=c;
	this.data = data;
	this.flag=flag;
	
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return flag.length;
		
		
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==((RelativeLayout) arg1);
	}
@Override
public Object instantiateItem(ViewGroup container, int position) {
	// TODO Auto-generated method stub
	TextView datatxt;
	ImageView imgflag;
	inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View itemView = inflator.inflate(R.layout.viewpager_item, container,false);
	datatxt = (TextView)itemView.findViewById(R.id.population);
	imgflag = (ImageView)itemView.findViewById(R.id.flag);
	
	datatxt.setText(data[position]);
	imgflag.setImageResource(flag[position]);
	((ViewPager) container).addView(itemView);
	return itemView;
}
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((RelativeLayout) object);		
		
	}
}
