package epfl.sweng.authentication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import epfl.sweng.R;
import epfl.sweng.R.layout;
import epfl.sweng.R.menu;
import epfl.sweng.servercomm.HttpCommunications;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AuthenticationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authentication, menu);
		return true;
	}
	
	public void requete(){
		HttpResponse reponse = null;
		
		try {
			reponse = HttpCommunications.getHttpResponse(HttpCommunications.URL_TEQUILA);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(reponse != null){
			
			
			
		}
		
		
	}

}
