package fluper.webradic.keepcleanf;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainSPLash extends Activity {
		
	
	TextView txtdemo;
	private int mindex=0;
	private String mtxt="Keep Clean";
	private int mdelay=150;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_splash);
		txtdemo=(TextView)findViewById(R.id.txtdemo);
		View vw = findViewById(R.id.bbbbbbb);
		Drawable dddd=  vw.getBackground();
		dddd.setAlpha(50);
		mh.removeCallbacks(chadd);
		mh.postDelayed(chadd, 0);
		
	}
	
	private Handler	mh = new Handler();
	 private Runnable chadd= new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtdemo.setText(mtxt.subSequence(0, mindex++));
				if(mindex<=mtxt.length()){
					
					mh.postDelayed(chadd, mdelay);
				}
				
			}
		};
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_splash, menu);
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
