package fluper.webradic.keepcleanf;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ReSplash1 extends Activity {

	ViewPager viewPager;
	PagerAdapter adapter;
	String[] data;
	int[] flag;
 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_re_splash1);
		data = new String[] { "Keepclean 1", "Keepclean 2", "Keepclean 3", "Keepclean 4", "Keepclean 5", "Keepclean 6"};
		flag = new int[]{
				R.drawable.mosfet,R.drawable.facebook,R.drawable.facebooker,R.drawable.ic_launcher,R.drawable.mosfet,R.drawable.facebooker
		};
		
		viewPager= (ViewPager)findViewById(R.id.pager);
		View bckk = findViewById(R.id.bcbbc);
		Drawable bkcc= bckk.getBackground();
		bkcc.setAlpha(50);
		adapter=new ViewPagerAdapter(ReSplash1.this,data,flag);
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.re_splash1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
