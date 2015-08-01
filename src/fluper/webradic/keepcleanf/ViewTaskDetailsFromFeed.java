package fluper.webradic.keepcleanf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ViewTaskDetailsFromFeed extends Activity {

	static String status;
	static Bundle b;
	ImageView iv,taskimg;
	static String file,mailfrom;
	TextView location,comments,email;
	static ProgressDialog pgd;
	static Button takeinit;
	 customad adapterr;  
	ListView mylist;
	
	
	public  List<bid> bidlist = new ArrayList<bid>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_task_details_from_feed);
		takeinit=(Button)findViewById(R.id.takeinit);
		iv=(ImageView)findViewById(R.id.uploadprog);
		iv.setImageResource(R.drawable.ic_check_circle_white_24dp);
		int color = Color.parseColor("#4ccd98"); //The color u want             
		iv.setColorFilter(color);
		 b = getIntent().getExtras();
		location = (TextView)findViewById(R.id.loctxt);
		
		location.setText(b.getString("location"));
		file=b.getString("file");
		GlobalLoginmanager.email="ajfh.majd@als.com";
		taskimg=(ImageView)findViewById(R.id.taskimg);
		comments=(TextView)findViewById(R.id.commentstxt);
		email=(TextView)findViewById(R.id.email);
		comments.setText(b.getString("comment"));
		mailfrom=b.getString("email");
		email.setText(mailfrom);
		
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
		ImageLoader imageLoader = ImageLoader.getInstance();

		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher).build();
		pgd=new ProgressDialog(ViewTaskDetailsFromFeed.this);
		pgd.setCancelable(false);
		adapterr = new customad(this, bidlist);
		imageLoader.displayImage(b.getString("url"), taskimg,options);
		mylist = (ListView)findViewById(R.id.bidslistfinal);
		mylist.setAdapter(adapterr);
		checkforinit();
		checkbids();
		
		
		takeinit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				HashMap<String, String> params=new HashMap<String,String>();
				params.put("mail", GlobalLoginmanager.email);
				params.put("file", file);
				pgd.show();
				JsonObjectRequest jreq=new JsonObjectRequest(Method.POST,"http://192.168.0.102/init/takeinit.php", new JSONObject(params), new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						// TODO Auto-generated method stub
						try {
							Log.e("onresponse of take init",arg0.getString("OK"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pgd.dismiss();
						checkforinit();
						checkbids();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						checkforinit();
					}
				});
				
				AppController.getInstance().addToRequestQueue(jreq);
			}
			
		});
	
		
	}
	
	 private void checkforinit() {
		// TODO Auto-generated method stub
		 pgd.setMessage("Loading");
		 pgd.show();
		 HashMap<String, String> params= new HashMap<String,String>();
		 params.put("mail", GlobalLoginmanager.email);
		 params.put("file", file);
		 JsonObjectRequest jreq=new JsonObjectRequest(Method.POST,"http://192.168.0.102/init/near2.php", new JSONObject(params), new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject result) {
				// TODO Auto-generated method stub
				pgd.dismiss();
				try {
					status = result.getString("show");
					 Log.e("status", status);
					if(status=="true")
						 takeinit.setVisibility(View.VISIBLE);
					if(status=="false")
						takeinit.setVisibility(View.INVISIBLE);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("JSONEXception at vtdff", e.toString());
				}
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.e("ViewTaskDetailsfromFeed Volley error", arg0.toString());
				
			
			}
		});
		 
		 
		 AppController.getInstance().addToRequestQueue(jreq);
		
		
	}
	 
	 public class customad extends BaseAdapter{

		 private Activity act;
		 private LayoutInflater inflater;
		 private List<bid> biditems;
		 public customad(Activity act, List<bid> biditems) {
			// TODO Auto-generated constructor stub
			 
			 this.act = act;
			 this.biditems = biditems;
		}
		 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return biditems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return biditems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if(inflater==null)
			inflater=(LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 if (convertView == null)
		            convertView = inflater.inflate(R.layout.list_row, null);
			 TextView title=(TextView)convertView.findViewById(R.id.title);
			 TextView rating = (TextView)convertView.findViewById(R.id.rating);
			 
			 bid bider = biditems.get(position);
			 title.setText(bider.getTitle());
			 rating.setText(String.valueOf(bider.getRating()));
			 
			 return convertView;
		}
		 
		 
		 
		 
	 }
	void checkbids(){
		 HashMap<String, String> paramsbid = new HashMap<String,String>();
		 paramsbid.put("file", file);
		 pgd.setMessage("Loading...");
		 pgd.show();
		 Log.e("file sent from checkbids", file);
		 JsonObjectRequest bidreq = new JsonObjectRequest(Method.POST, "http://192.168.0.102/near/bider.php", new JSONObject(paramsbid), new Response.Listener<JSONObject>() {

			

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.e("checkbids Response", response.toString());
				
				for(int i =0; i<response.length()-1; i++){
					try {
						pgd.dismiss();
						JSONObject jobj=response.getJSONObject(i+"");
						bid lobid= new bid();
						lobid.setTitle(jobj.getString("username"));
						lobid.setRating(5.0);
						bidlist.add(lobid);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("JSONexception at checkbids", e.toString());
						
					}
					
				}
				
				
				adapterr.notifyDataSetChanged();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				
				Log.e("Volleyerror at checkbids", arg0.toString());
			}
		});
		 
		 AppController.getInstance().addToRequestQueue(bidreq);
		 
	 }
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_task_details_from_feed, menu);
		
		
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
