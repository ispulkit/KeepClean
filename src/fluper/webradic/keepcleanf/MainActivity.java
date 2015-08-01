package fluper.webradic.keepcleanf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
public class MainActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, OnClickListener {

	private static final int PROFILE_PIC_SIZE = 400;
	private static final int RC_SIGN_IN = 0;
	private static boolean gplussigninclicked;
	private SignInButton signbtn;
	static Uri imguri;
	Button capbtn, fblogin;
	SessionManagerclas smsob;
	TextView fgps;
	public static String path, pemail;
	static ConnectionResult mconresult;
	ImageView iv, iv2;
	static String imgurl;
	TextView tv1, tv2;
	Button loginbuttonok;
	static String emailhaha,passwordhaha;
	ProgressDialog pgd;
	ContentValues values;
	GoogleApiClient mgapiclient;
	private boolean intentinprogress;
	
	String fbappid = "1634007786875468";
	Facebook fb;
	EditText emailedlaw,passwordlaw;
	 AsyncFacebookRunner masyncfbrunner;
	String filenamefb = "AndroidSSO_data";
	SharedPreferences spf;
	Button reg;
	
	private static final int camcode = 2212;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		signbtn = (SignInButton) findViewById(R.id.btn_sign_in);
		fblogin = (Button)findViewById(R.id.fblogin);
		mgapiclient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		//signbtn.setOnClickListener(this);
		//signout.setOnClickListener(this);
			smsob=new SessionManagerclas(getApplicationContext());
		
		if(smsob.isLoggedIn()){
			
			Intent intenteroh = new Intent(MainActivity.this, AfterLogin.class);
            startActivity(intenteroh);
            finish();
			
		}
		capbtn = (Button) findViewById(R.id.clickbtn);
		capbtn.setOnClickListener(this);
		reg=(Button)findViewById(R.id.regbtn);
		fb= new Facebook(fbappid);
		fgps=(TextView)findViewById(R.id.forgot);
		passwordlaw=(EditText)findViewById(R.id.passwordlaw);
		pgd=new ProgressDialog(MainActivity.this);
		pgd.setCancelable(false);
		 masyncfbrunner= new AsyncFacebookRunner(fb);
		 emailedlaw=(EditText)findViewById(R.id.emailedlaw);
		 loginbuttonok=(Button)findViewById(R.id.loginokbtn);
		 
		 
		 View backgrnd = findViewById(R.id.backgrnd);
		 Drawable bckimg = backgrnd.getBackground();
		 bckimg.setAlpha(40);
		 loginbuttonok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 emailhaha=emailedlaw.getText().toString();
				 passwordhaha=passwordlaw.getText().toString();
					if(emailhaha.trim().length()>0&&passwordhaha.trim().length()>0){
					
					checkforcustomlogin(emailhaha,passwordhaha);
				}else{
					
					Toast.makeText(getApplicationContext(), "Please Enter Valid Credidentials", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		 
		 fgps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent iio=new Intent();
				iio.setClass(MainActivity.this, ForgotPassword.class);
				startActivity(iio);
				
			}
		});
		 
		 reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent();
				i.setClass(getApplicationContext(), CustomLogin.class);
				startActivity(i);  
				
			}
		});
		 
		fblogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				logintofb();
				
			}
		});
		 
	/*	emailedlaw.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasfocus) {
				// TODO Auto-generated method stub
				if(hasfocus){
					Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.demoanimm);
					
					passwordlaw.setAnimation(anim);
					Toast.makeText(MainActivity.this, "Got focus", Toast.LENGTH_LONG).show();
					
				}else{
					
					Toast.makeText(MainActivity.this, "Lost  focus", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		 */
	}

	void logintofb(){
		
		
		spf=getPreferences(MODE_PRIVATE);
		String acctoken= spf.getString("access_token", null);
		long expires = spf.getLong("access_expires", 0);
		if(acctoken!=null){
			
			
			fb.setAccessToken(acctoken);
			
		}
		
		if(expires!=0){
			
			fb.setAccessExpires(expires);
			getfbinfo();
		}
		
		if(!fb.isSessionValid()){
			
			fb.authorize(this, new String[]{"email","public_profile"}, new DialogListener() {
				
				@Override
				public void onFacebookError(FacebookError arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Facebook Error", Toast.LENGTH_SHORT).show();
					Log.e("Login fb error","Custom Tag");
					getfbinfo();
				}
				
				@Override
				public void onError(DialogError arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
					Log.e("Login error","Custom Tag");
					getfbinfo();
				}
				
				@Override
				public void onComplete(Bundle arg0) {
					// TODO Auto-generated method stub
					
					SharedPreferences.Editor ed= spf.edit();
					ed.putString("access_token", fb.getAccessToken());
					ed.putLong("access_expires", fb.getAccessExpires());
					ed.commit();
					Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
					Log.e("Login completed","On complete()");
					getfbinfo();
					
					
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "User has Cancelled FbLogin", Toast.LENGTH_SHORT).show();
					Log.e("Login cncled","Custom Tag");
					getfbinfo();
				}
			});
			
			
		}
		
		
		
		
		
		
		
	}
	
	
private void checkforcustomlogin(final String emailbooz, final String passwordbooz){
		
		pgd.setMessage("Logging in");
		pgd.show();
		Log.e("email pass in checkforcustomlogin function ", emailbooz+" "+passwordbooz);
		String tag_string_req = "req_login";
		
		StringRequest strReq = new StringRequest(Method.POST, configappdb.URL_REGISTER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				
				 Log.d("CustomLogin.java response error", "Login Response: " + response.toString());
                 pgd.dismiss();
                 
                 try{
                	 
                	 JSONObject jobj= new JSONObject(response);
                	 boolean error=jobj.getBoolean("error");
                	 
                	 if(!error){
                		 
                		 smsob.setLogin(true);
                		 
                		 Intent intenter=new Intent(MainActivity.this,AfterLogin.class);
                		 
                		 startActivity(intenter);
                		 finish();
                		 
                	 }else{
                		 
                		 String errormsg=jobj.getString("error_msg");
                		 Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
                		 
                		 
                		 
                	 }
                 }catch(JSONException e){
                	 
                	e.printStackTrace(); 
                	 
                 }
                 
				
			}
			
			
			
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
				Log.e("Some Volley error here",error.getMessage());
				
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
				pgd.cancel();
			}
		}){
			
			protected Map<String, String> getParams(){
				
				 Map<String, String> params = new HashMap<String, String>();
	                params.put("tag", "login");
	                params.put("email", emailbooz);
	                params.put("password", passwordbooz);
	 
	                return params;
				
			}
			
			
		};
		
		AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
	}
	
	
	void getfbinfo(){
		
		masyncfbrunner.request("me", new RequestListener() {
			
			@Override
			public void onMalformedURLException(MalformedURLException arg0, Object arg1) {
				// TODO Auto-generated method stub
				
				Log.e("Custom tag getting fb info","Malformed url");
				
			}
			
			@Override
			public void onIOException(IOException arg0, Object arg1) {
				// TODO Auto-generated method stub
				
				Log.e("Custom tag getting fbinfo","ioexception");
				
			}
			
			@Override
			public void onFileNotFoundException(FileNotFoundException arg0, Object arg1) {
				// TODO Auto-generated method stub
				
				Log.e("Custom tag getting fbinfo", "filenotfoundException");
				
			}
			
			@Override
			public void onFacebookError(FacebookError arg0, Object arg1) {
				// TODO Auto-generated method stub
				
				Log.e("CUSTOM TAG getting fb info", "some fberror");
			}
			
			@Override
			public void onComplete(String response, Object state) {
				
				Log.e("Profile",response);
				String json=response;
				
				try{
					
					JSONObject profile= new JSONObject(json);
					final String name=profile.getString("name");
					final String id=profile.getString("id");
					final String email=profile.getString("email");
					Log.e("User info obtained: ",name+" , "+email);
					
					try {
						URL fbimgurl=new URL("https://graph.facebook.com/"+id+"/picture?type=large");
						final Bitmap fbimgbitmap=BitmapFactory.decodeStream(fbimgurl.openConnection().getInputStream());
						/*runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								ll.setVisibility(View.VISIBLE);
								iv.setVisibility(View.VISIBLE);
								iv.setImageBitmap(fbimgbitmap);
								
							}
							
							
							
							
						});*/
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("fb image error","MalformedURL");
					}catch (IOException e){
						
						e.printStackTrace();
						Log.e("fb Image error","IO Exception");
					}
					
					runOnUiThread(new Runnable() {
						
						@Override
							public void run() {
							// TODO Auto-generated method stub
							
							Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
							
						}
					});
					
				}catch(JSONException e){
					
						e.printStackTrace();
						Log.e("JSON Error :O",e.toString());
				}
				
				updateui(true);
				
			}
		});
		
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		mgapiclient.connect();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mgapiclient.isConnected())
			mgapiclient.disconnect();

		
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.btn_sign_in:
			signinwithgplus();
			break;

		/*case R.id.btn_sign_out:
			signoutofgplus();
			break;
	*/	case R.id.clickbtn:
			camprocess();
			break;

		}

	}

	public void camprocess() {

		Intent camint = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		values = new ContentValues();
		values.put(MediaColumns.TITLE, "New Pic");
		values.put(ImageColumns.DESCRIPTION, "From your cam");
		imguri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		camint.putExtra(MediaStore.EXTRA_OUTPUT, imguri);
		startActivityForResult(camint, camcode);

	}

	public void signinwithgplus() {

		if (!mgapiclient.isConnecting()) {
			gplussigninclicked = true;
			resolvesigninerror();
		}

	}

	public void resolvesigninerror() {

		if (mconresult.hasResolution()) {
			intentinprogress = true;
			try {
				mconresult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				intentinprogress = false;
				mgapiclient.connect();
			}

		}
	}

	public void signoutofgplus() {

		if (mgapiclient.isConnected()) {

			Plus.AccountApi.clearDefaultAccount(mgapiclient);
			mgapiclient.disconnect();
			mgapiclient.connect();
			updateui(false);
		}

	}

	

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (!result.hasResolution()) {

			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}
		if (!intentinprogress) {

			mconresult = result;
			if (gplussigninclicked) {

				resolvesigninerror();
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		
		if(requestCode!=RC_SIGN_IN&&requestCode!=camcode){
		fb.authorizeCallback(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
		}
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				gplussigninclicked = false;

			}

			intentinprogress = false;
			if (!mgapiclient.isConnecting()) {
				mgapiclient.connect();

			}
		}

		if (requestCode == camcode) {
			if (resultCode == RESULT_OK) {

				Bitmap thumbnail = null;

				try {
					thumbnail = MediaStore.Images.Media.getBitmap(
							getContentResolver(), imguri);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				imgurl = getpathfromuri(imguri);
				Toast.makeText(getApplicationContext(),
						"Image Saved at: " + imgurl, Toast.LENGTH_LONG).show();
				iv2.setVisibility(View.VISIBLE);
				iv2.setImageBitmap(thumbnail);

				Upload upss = new Upload(imgurl);
				Log.d("File Path Sent: ", imgurl);

				gpstracker gps = new gpstracker(this);
				if (gps.canGetLocation()) {

					double lati = gps.getLatitude();
					double longi = gps.getLongitude();
					if (lati != 0.0 && longi != 0.0) {

						Toast.makeText(
								getApplicationContext(),
								"Your Location is :" + lati + " ," + longi
										+ " ", Toast.LENGTH_SHORT).show();
						Intent uint = new Intent();
						uint.setClass(this, Upload.class);
						Bundle extras = new Bundle();
						// extras.putParcelable("load", imguri);
						extras.putString("load", imgurl);
						extras.putDouble("lat", lati);
						extras.putDouble("longi", longi);
						extras.putString("mail", pemail);
						uint.putExtras(extras);
						startActivity(uint);
					} else {

						gps.showSettingsAlert();
					}
				}

				else {

					gps.showSettingsAlert();
				}

			}

		}

	}

	public String getpathfromuri(Uri imguri2) {

		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(imguri2, proj, null, null, null);
		int col_index = cursor
				.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(col_index);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
		outState.putParcelable("Uri", imguri);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// super.onRestoreInstanceState(savedInstanceState);
		imguri = savedInstanceState.getParcelable("Uri");

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

		intentinprogress = false;
		gplussigninclicked = false;
		Toast.makeText(this, "User Connected.", Toast.LENGTH_LONG).show();
		getuserinfofromgplus();
		updateui(true);
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

		mgapiclient.connect();
		updateui(false);

	}

	public void updateui(boolean cond) {
		if (cond) {
			
			Intent iafter=new Intent();
			iafter.setClass(getApplicationContext(), AfterLogin.class);
			startActivity(iafter);
			
		/*	signbtn.setVisibility(View.GONE);
			signout.setVisibility(View.VISIBLE);
			revokeacc.setVisibility(View.VISIBLE);
			capbtn.setVisibility(View.VISIBLE);
			ll.setVisibility(View.VISIBLE);
*/
		} else {

		/*	signbtn.setVisibility(View.VISIBLE);
			signout.setVisibility(View.GONE);
			revokeacc.setVisibility(View.GONE);
			ll.setVisibility(View.GONE);
		*/
		}

	}

	public void getuserinfofromgplus() {

		if (Plus.PeopleApi.getCurrentPerson(mgapiclient) != null) {
			Person currentperson = Plus.PeopleApi.getCurrentPerson(mgapiclient);
			String pname = currentperson.getDisplayName();
			String pphotourl = currentperson.getImage().getUrl();
			String pgplusprofile = currentperson.getUrl();
			pemail = Plus.AccountApi.getAccountName(mgapiclient);

			Log.d("Info ", "Fetched: " + pname + "Photo URL: " + pphotourl
					+ "Email: " + pemail + "Profile: " + pgplusprofile);
			//tv1.setText(pname);
			//tv2.setText(pemail);

			pphotourl = pphotourl.substring(0, pphotourl.length() - 2)
					+ PROFILE_PIC_SIZE;
			new loadpic().execute(pphotourl);

		} else {

			Toast.makeText(this, "Person Info. is Null", Toast.LENGTH_LONG)
					.show();
		}

	}

	class loadpic extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			String urldisp = urls[0];
			Bitmap bmp = null;

			try {
				InputStream in = new java.net.URL(urldisp).openStream();
				bmp = BitmapFactory.decodeStream(in);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Error in AsyncTask",
						"InputStreamorImageLoadviaNet Custom Error Tag");
			}

			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub

			iv.setImageBitmap(result);

		}
	}

}
