package fluper.webradic.keepcleanf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Upload extends Activity {
	static String serverResponseMessage = "";

	ProgressDialog prgDialog;
	String encodedString;
	static RequestParams paramstwo = new RequestParams();
	String fileName;
	Bitmap bitmap;
	static double longi, lati;
	public String imgurl;

	public Upload(String imgurl) {
		imgurl = this.imgurl;

	}

	public Upload() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		prgDialog = new ProgressDialog(this);
		// Set Cancelable as False
		prgDialog.setCancelable(false);
		String imgurl = getIntent().getStringExtra("load");
		Bundle b = getIntent().getExtras();
		longi = b.getDouble("longi");
		lati = b.getDouble("lat");
		prgDialog.setMessage("Altering Image to binary");
		prgDialog.show();
		String mail = b.getString("mail");
		paramstwo.put("mail", mail);
		new uploader().execute(imgurl);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	class uploader extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String path = params[0];

			String fileNameSegments[] = path.split("/");
			fileName = fileNameSegments[fileNameSegments.length - 1];
			paramstwo.put("filename", fileName);
			paramstwo.put("comments", "secondcommmmm");
			paramstwo.put("longi", longi + " ");
			paramstwo.put("lati", lati + " ");
			BitmapFactory.Options options = null;
			options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			bitmap = BitmapFactory.decodeFile(path, options);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			// Must compress the Image to reduce image size to make upload easy
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byte_arr = stream.toByteArray();
			// Encode Image to String
			encodedString = Base64.encodeToString(byte_arr, 0);

			Geocoder gc = new Geocoder(getApplicationContext());
			try {

				List<Address> addresses = gc.getFromLocation(lati, longi, 1);
				if (addresses != null) {
					
					Address fetchedadd = addresses.get(0);
					StringBuilder strb = new StringBuilder();

					for (int i = 0; i < fetchedadd.getMaxAddressLineIndex(); i++) {

						strb.append(fetchedadd.getAddressLine(i)).append("\n");

					}
					Log.e("Got address: ", strb.toString());
					paramstwo.put("address", strb.toString() + " ");
				} else {
					paramstwo.put("address", " ");
					Log.e("Can't fetch address: ", ":|");

				}

			} catch (IOException e) {

				e.printStackTrace();
				Log.e("Error in Geocode", e.toString());

			}

			return "";

		}

		@Override
		protected void onPostExecute(String msgg) {
			// TODO Auto-generated method stub
			prgDialog.setMessage("Calling Upload");
			// Put converted Image string into Async Http Post param
			paramstwo.put("image", encodedString);
			triggerImageUpload();

			// Intent i2= new Intent();
			// i2.setClass(Upload.this, Fetching_address.class);

		}

	}

	public void triggerImageUpload() {
		makeHTTPCall();
	}

	public void makeHTTPCall() {
		prgDialog.setMessage("Invoking Php");
		AsyncHttpClient client = new AsyncHttpClient();
		// Don't forget to change the IP address to your LAN address. Port no as
		// well.
		client.post("http://www.fluper.com/imgfileupload/imgupload.php",
				paramstwo, new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {
						prgDialog.hide();
						Toast.makeText(getApplicationContext(), response,
								Toast.LENGTH_LONG).show();
						Log.e("Server's SUCCess Response CUs", response);
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						prgDialog.hide();
						if (statusCode == 404) {
							Toast.makeText(getApplicationContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}
						else if (statusCode == 500) {
							Toast.makeText(getApplicationContext(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						}
						else {
							Toast.makeText(
									getApplicationContext(),
									"Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
											+ statusCode, Toast.LENGTH_LONG)
									.show();
						}
					}
				});
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (prgDialog != null) {
			prgDialog.dismiss();
		}
	}
}
