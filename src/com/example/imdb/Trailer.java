package com.example.imdb;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;





public class Trailer extends Activity {
	private ProgressDialog pDialog;
	
	
	// results JSONArray
	   JSONArray results = null;
	
	// JSON node keys
		private static final String TAG_ID = "id";
		
		
		private static final String TAG_RESULTS = "results";
		private static final String TAG_KEY = "key";
		
	
	private static final String TAG_TRAILER_URL1="http://api.themoviedb.org//3/movie/";
	private static final String TAG_TRAILER_URL3="/videos?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
	
	private static String TAG_TRAILER_URL="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.trailer_layout);
		  // getting intent data
	        Intent in2 = getIntent();
	        // Get JSON values from previous intent
	        String id = in2.getStringExtra(TAG_ID);
	        TAG_TRAILER_URL=TAG_TRAILER_URL1+id+TAG_TRAILER_URL3;
	        
	      
	   
	        
	        // Calling async task to get json
     		new GetTrailer().execute();
		 
	}
	
	
	private class GetTrailer extends AsyncTask<Void, Void, Void>{
		
		
	String	keylbl;
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(Trailer.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// Creating service handler class instance
			serviceHandler sh5 = new serviceHandler();
		
			
			// Making a request to url and getting response
			String jsonStr = sh5.makeServiceCall(TAG_TRAILER_URL, serviceHandler.GET);
			
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					JSONObject jsonObj  = new JSONObject(jsonStr);
					
					// Getting JSON Array node
					results = jsonObj.getJSONArray(TAG_RESULTS);
					// looping through All Contacts
					for (int i = 0; i < results.length(); i++) {
						JSONObject c = results.getJSONObject(i);
                        keylbl = c.getString(TAG_KEY);
					}
				}
				catch(JSONException e){
					e.printStackTrace();
				}
			}else {
					Log.e("ServiceHandler", "Couldn't get any data from the url");
				}
					
				
	
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			
		        
		        Button btn02 =(Button) findViewById(R.id.btn2);
		     
		        
		       
		        btn02.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v="+keylbl)));
			        	    Log.i("Video", "Video Playing....");

					}
				});
			
			
			
   }
           }
		
		
		}
	




