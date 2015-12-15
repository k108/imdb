package com.example.imdb;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class details extends Activity{
	
	private ProgressDialog pDialog;
	Button btn;
	
	// results JSONArray
	   JSONArray results = null;
	
	   // JSON Node names of results array
		private static final String TAG_RESULTS = "results";
		private static final String TAG_ADULT = "adult";
		private static final String TAG_BACKDROP_PATH = "backdrop_path"; 
		private static final String TAG_ORIGINAL_TITLE =  "original_title";
		private static final String TAG_RELEASE_DATE = "release_date";
		private static final String TAG_POPULARITY = "popularity";
		private static final String TAG_TITLE = "title";
		private static final String TAG_VOTE_AVERAGE = "vote_average";
		private static final String TAG_PHONE_VOTE_COUNT="vote_count";
		private static final String TAG_OVERVIEW ="overview";
		private static final String TAG_STATUS ="status";
		private static final String TAG_RUNTIME ="runtime";
		private static final String TAG_BUDGET ="budget";
		private static final String TAG_TAGLINE ="tagline";
	
	// JSON node keys
	private static final String TAG_ID = "id";
	private static final String TAG_IMAGE_PATH="http://d3gtl9l2a4fn1j.cloudfront.net/t/p/w500";
	private static final String TAG_POSTER_PATH="backdrop_path";
	
	
	private static final String TAG_DETAILS_URL1="http://api.themoviedb.org/3/movie/";
	private static final String TAG_DETAILS_URL3="?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
	private static String TAG_DETAILS_URL="";
	private static String TAG_IMAGE_URL="";
	
	
	

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
       String id = in.getStringExtra(TAG_ID);
        TAG_DETAILS_URL=TAG_DETAILS_URL1+id+TAG_DETAILS_URL3;
        
        
        btn= (Button) findViewById(R.id.button1);
        
    	btn.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			// Starting trailer activity
    			Intent in2 = new Intent(getApplicationContext(),
    					Trailer.class);
    			 // getting intent data
    	        Intent in = getIntent();
    			String id = in.getStringExtra(TAG_ID);
    			in2.putExtra(TAG_ID,id);
    			startActivity(in2);
    			
    		}});
        
        
    	
  
        
     // Calling async task to get json
     		new GetDetails().execute();
     		
       
        
      
}



private class GetDetails extends AsyncTask<Void, Void, Void> {
	String orgTitle;
	String title;
	String releaseDate;
	String Adult;
	String voteAverage;
	String PhoneVoteCount;
	String Popularity;
	String Poster;
	Bitmap img;
	String overvw;
	String bdgt;
	String stts;
	String rntm;
	String tgln;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Showing progress dialog
		pDialog = new ProgressDialog(details.this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();

	}
	

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		// Creating service handler class instance
					serviceHandler sh = new serviceHandler();
					serviceHandler sh1 = new serviceHandler();
					
					
					
					

					// Making a request to url and getting response
					String jsonStr = sh.makeServiceCall(TAG_DETAILS_URL, serviceHandler.GET);

					Log.d("Response: ", "> " + jsonStr);

					if (jsonStr != null) {
						try {
							JSONObject c  = new JSONObject(jsonStr);
						
								
		
								orgTitle = c.getString(TAG_ORIGINAL_TITLE);
							    releaseDate = sh.reverseDate(c.getString(TAG_RELEASE_DATE));
								voteAverage = c.getString(TAG_VOTE_AVERAGE);
								PhoneVoteCount = c.getString(TAG_PHONE_VOTE_COUNT);
								overvw=c.getString(TAG_OVERVIEW);
								stts=c.getString(TAG_STATUS);
								rntm=c.getString(TAG_RUNTIME);
								bdgt=c.getString(TAG_BUDGET);
								tgln=c.getString(TAG_TAGLINE);
								
								Adult=c.getString(TAG_ADULT);
								Popularity=c.getString(TAG_POPULARITY);
								title= c.getString(TAG_TITLE);
								TAG_IMAGE_URL=c.getString(TAG_POSTER_PATH);
								Poster=TAG_IMAGE_PATH+TAG_IMAGE_URL;
							
								img=sh.getBitmapFromURL(Poster);

							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
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
					/**
					 * Updating parsed JSON data into TextViews
					 * */
					  // Displaying all values on the screen
			        TextView lbltitle = (TextView) findViewById(R.id.title_lable);
			        TextView lblorgTitle = (TextView) findViewById(R.id.orgttle_lable);
			        TextView lblreleaseDate = (TextView) findViewById(R.id.reldt_lable);
			        TextView lbladult = (TextView) findViewById(R.id.adlt_lable);
			        TextView lblpopularity = (TextView) findViewById(R.id.pplrty_lable);
			        TextView lblvoteAverage = (TextView) findViewById(R.id.phnavg_lable);
			        TextView lblphoneVoteCount = (TextView) findViewById(R.id.phnvtcnt_lable);
			        TextView lblovervw=(TextView) findViewById(R.id.overvw_lable);
			        TextView lblstatus=(TextView) findViewById(R.id.status_lable);
			        TextView lblruntime=(TextView) findViewById(R.id.runtime_lable);
			        TextView lblbdgt=(TextView) findViewById(R.id.bdgt_lable);
			        TextView lbltgln=(TextView) findViewById(R.id.tgln_lable);


			  					
				
			        
					
			        lbltitle.setText(title);
			        lblorgTitle.setText(orgTitle);
			        lblreleaseDate.setText("Release Date: "+releaseDate);
			        lbladult.setText("Adult: "+Adult);
			        lblpopularity.setText("Popularity: "+Popularity);
			        lblvoteAverage.setText("Vote Average: "+voteAverage);
			        lblphoneVoteCount.setText("Phone Vote Count: "+PhoneVoteCount);
			        lblovervw.setText("Overview: "+overvw);
			        lblstatus.setText("Status: "+stts);
			        lblruntime.setText("Runtime: "+rntm+" min");
			        lblbdgt.setText("Budget: $"+bdgt);
			        lbltgln.setText("Tagline: "+tgln);
			        
			        ImageView imgv=(ImageView) findViewById(R.id.imgVw);
			        
			        imgv.setImageBitmap(img);
			        
			 
			        

	    }
			
			    	}
			    	
	}

					
					

	

	


