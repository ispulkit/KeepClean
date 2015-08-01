package fluper.webradic.keepcleanf;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManagerclas {

	private static String TAG = SessionManagerclas.class.getSimpleName();
	
	SharedPreferences pref;
	Editor ed9;
	Context context9;
	
	int PRIVATE_MODE=0;
	
	private static final String PREF_NAME = "CustomLogin";
    
    private static final String KEY_IS_LOGGEDIN = "isCustomLoggedIn";
 
    
    public SessionManagerclas(Context context){
    	
    	this.context9=context;
    	pref=context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    	ed9=pref.edit();
    	
    	
    }
    
	public void setLogin(boolean isloginvalue){
		
		ed9.putBoolean(KEY_IS_LOGGEDIN, isloginvalue);
		ed9.commit();
		Log.d(TAG, "UserLoginSession Modified");
		
		
	}
    
	public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
