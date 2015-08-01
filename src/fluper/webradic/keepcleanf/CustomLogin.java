package fluper.webradic.keepcleanf;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomLogin extends Activity {

	EditText emailed0,name0,pw0;
	Button signup;
	ProgressDialog pgd;
	static StringRequest strReq;
	static String tag_string_req;
	private SessionManagerclas session;
	static String passwordhaha,namehaha,emailhaha,phonehaha;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_login);
		emailed0=(EditText)findViewById(R.id.emailed);
		signup=(Button)findViewById(R.id.submitbtn);
		name0=(EditText)findViewById(R.id.namesignup);
		pw0=(EditText)findViewById(R.id.pwsignup);
		pgd=new ProgressDialog(CustomLogin.this);
		pgd.setCancelable(false);
		session=new SessionManagerclas(getApplicationContext());
		if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(CustomLogin.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
 
		
		signup.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				passwordhaha=pw0.getText().toString();
				namehaha=name0.getText().toString();
				emailhaha=emailed0.getText().toString();
				
				 if (!namehaha.isEmpty() && !emailhaha.isEmpty() && !passwordhaha.isEmpty()) {
	                    registerUser(namehaha, emailhaha, passwordhaha);
				 }else{
					 
					 Toast.makeText(getApplicationContext(), "Enter Valid Details", Toast.LENGTH_SHORT).show();
					 
				 }
				
			}
		});
		
	
	}


	
	private void registerUser(final String name, final String email,final String password){
		
		 tag_string_req= "req_register";
		
		pgd.setMessage("Registering");
		pgd.show();
		
		  strReq= new StringRequest(Method.POST, configappdb.URL_REGISTER, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.d("Register Response",response.toString());
				pgd.dismiss();
				try{
					 JSONObject jObj = new JSONObject(response);
                     boolean error = jObj.getBoolean("error");
					 
                     if(!error){
                    	 String uid=jObj.getString("uid");
                    	 
                    	 JSONObject user=jObj.getJSONObject("user");
                    	 String name=user.getString("name");
                    	 String email=user.getString("email");
                    	 String created_at=user.getString("created_at");
                    	 Toast.makeText(CustomLogin.this, "Registered", Toast.LENGTH_SHORT).show();
                    	 Intent i = new Intent(CustomLogin.this,MainActivity.class);
                    	 startActivity(i);
                    	 finish();
                    	 
                    	 
                     }else{
                    	 
                    	 
                    	 String errmsg=jObj.getString("error_msg");
                    	 Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT).show();
                     }
                     
					
				}catch(JSONException e){
					e.printStackTrace();
					
				}
			
			}}
		,new Response.ErrorListener() {
				 
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("CustomLogin/RegisterResponse Error", "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    pgd.dismiss();
                }
		}){
			
			
			 @Override
	            protected Map<String, String> getParams() {
	                // Posting params to register url
	                Map<String, String> params = new HashMap<String, String>();
	                params.put("tag", "register");
	                params.put("name", name);
	                params.put("email", email);
	                params.put("password", password);
	                return params;
	            }
		};
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_login, menu);
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
