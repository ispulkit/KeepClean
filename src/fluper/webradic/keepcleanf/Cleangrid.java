package fluper.webradic.keepcleanf;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class Cleangrid extends Activity implements OnClickListener{

	ImageView imageView,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8;
	static double lati,longi;
	static String urls[]= new String[11];
	static String TS[]= new String[11];
	static String emails[]= new String[11];
	static String comments[]= new String[11];
	static String distance[]= new String[11];
	static String locations[] = new String[11];
	static String status[] = new String[11];
	static String type[] = new String[11];
	static Intent i;
	static Bundle b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cleangrid);
		 i = new Intent();
		i.setClass(Cleangrid.this, ViewTaskDetailsFromFeed.class);
		b=new Bundle();
		gpstracker gps = new gpstracker(Cleangrid.this);
		if(gps.canGetLocation()){
			lati=gps.getLatitude();
			longi=gps.getLongitude();
			Log.e("Recieved latlng from gpstracker", lati+" "+longi+"");
			if(lati!=0.0&&longi!=0.0){
				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
						getApplicationContext())
						.defaultDisplayImageOptions(defaultOptions)
						.memoryCache(new WeakMemoryCache())
						.discCacheSize(100 * 1024 * 1024).build();

				ImageLoader.getInstance().init(config);
					thingsaresimple();
			
				
				
			}else{
				
				
			gps.showSettingsAlert();
				
				
			}
			
		}
		
		
		
	}
	
	
	void thingsaresimple() {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userlng", longi+"");
		params.put("userlat", lati+"");
        Log.e("Sent data", (new JSONObject(params)).toString());
		JsonObjectRequest jreq=new JsonObjectRequest(Method.POST, "http://192.168.0.102/near/near2.php",  new JSONObject(params), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.e("Cleangrid", response.toString());
				Log.e("Cleangrid repsponse.length", response.length()+"");
				try{
					for(int j=0;j<=1;j++)
					{JSONObject link=response.getJSONObject(j+"");
					urls[j]=link.getString("fn");
					TS[j]=link.getString("TS");
					emails[j]=link.getString("email");
					comments[j]=link.getString("comment");
					distance[j]=link.getString("dist");
					locations[j]=link.getString("location");
					status[j]=link.getString("status");
					type[j]=link.getString("type");
					
					}
					updateview();
				}catch(JSONException e){
					e.printStackTrace();
					Log.e("JSONEXCEPTION in Cleangrid", e.toString());
					
				}
				
				
			
			}
		}, new com.android.volley.Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			
		
		AppController.getInstance().addToRequestQueue(jreq);
		
	}

	private void updateview(){
		
		String url = "http://javatechig.com/wp-content/uploads/2014/05/UniversalImageLoader-620x405.png";
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisc(true).resetViewBeforeLoading(true)
						.showImageForEmptyUri(R.drawable.ic_launcher)
						.showImageOnFail(R.drawable.ic_launcher)
						.showImageOnLoading(R.drawable.ic_launcher).build();
				
		ImageView imageView = (ImageView) findViewById(R.id.jdas);		
		//ImageView imageView1 = (ImageView) findViewById(R.id.jdas111);		
		ImageView imageView2= (ImageView) findViewById(R.id.jdas2);		
		//ImageView imageView3 = (ImageView) findViewById(R.id.jdas3);		
		//ImageView imageView4 = (ImageView) findViewById(R.id.jdas112);		
		ImageView imageView5 = (ImageView) findViewById(R.id.jdas5);		
		//ImageView imageView6 = (ImageView) findViewById(R.id.jdas6);		
		//ImageView imageView7 = (ImageView) findViewById(R.id.jdas7);		
		//ImageView imageView8 = (ImageView) findViewById(R.id.jdas8);		
		
		imageLoader.displayImage("http://192.168.0.102/imgfileupload/uploads/"+urls[0], imageView, options);
		//imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/1435755764442.jpg", imageView1, options);
		imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/1435756353799.jpg", imageView5, options);
		imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/1435756554105.jpg", imageView2, options);
		//imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/1435817085731.jpg", imageView3, options);
		//imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/1436009023947.jpg", imageView4, options);
		//imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/Chotu%2020150613_011413.jpg", imageView6, options);
		//imageLoader.displayImage("http://www.fluper.com/imgfileupload/uploads/Danish%20MSIT%2020150622_224146.jpg", imageView7, options);
		//imageLoader.displayImage("http://192.168.0.102/imgfileupload/uploads/Code-Wallpaper.jpg", imageView8, options);
		imageView.setOnClickListener(this);
			
		imageView2.setOnClickListener(this);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cleangrid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.jdas:
			sendintent(0);
			break;
			
		case R.id.jdas2: 
			sendintent(1);
			break;
		}
		
		
	}
void sendintent(int check){
	
	b.putString("url", "http://192.168.0.102/imgfileupload/uploads/"+urls[check]);
	b.putString("TS", TS[check]);
	b.putString("location", locations[check]);
	b.putString("type", type[check]);
	b.putString("status", status[check]);
	b.putString("comment", comments[check]);
	b.putString("email", emails[check]);
	b.putString("file", urls[check]);
	i.putExtras(b);
	startActivity(i);
	
	
}

	
	}

