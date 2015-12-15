package com.example.imdb;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends ListActivity{
	private ProgressDialog pDialog;
	
	
	// URL to get nowPlaying JSON
	String current_url;
		private static final String url = "http://api.themoviedb.org/3/movie/now_playing?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
		private static final String url_top ="http://api.themoviedb.org/3/movie/top_rated?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
		private static final String url_upcmg="http://api.themoviedb.org/3/movie/upcoming?api_key=f47dd4de64c6ef630c2b0d50a087cc33";
	// JSON Node names of dates array
	private static final String TAG_DATES = "dates";
		private static final String TAG_MINIMUM =  "minimum";
		private static final String TAG_MAXIMUM = "maximum";
		
   // JSON Node names of page array
		 private static final String TAG_PAGE ="page";
			
   // JSON Node names of results array
	private static final String TAG_RESULTS = "results";
	private static final String TAG_ADULT = "adult";
	private static final String TAG_BACKDROP_PATH = "backdrop_path"; 
	private static final String TAG_ID = "id";
	private static final String TAG_ORIGINAL_TITLE =  "original_title";
	private static final String TAG_RELEASE_DATE = "release_date";
	
	private static final String TAG_POSTER_PATH="poster_path";
	private static final String TAG_POPULARITY = "popularity";
	private static final String TAG_TITLE = "title";
	private static final String TAG_VOTE_AVERAGE = "vote_average";
	private static final String TAG_PHONE_VOTE_COUNT="vote_count";
	
	// JSON Node names of total_pages array
	private static final String TAG_TOTAL_PAGES ="total_pages";
			
	// JSON Node names of total_results array
	private static final String TAG_TOTAL_RESULTS ="total_results";
	
	
	// dates JSONArray
	   JSONArray dates= null;
	
	// results JSONArray
	   JSONArray results = null;

	// Hashmap for ListView
		ArrayList<HashMap<String, String>> movieList;



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.options_menu, menu);
		    return true;


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
    	
    	 switch (item.getItemId()) {
    		case R.id.nowplyng:
    			Toast.makeText(getApplicationContext(), "now playing",Toast.LENGTH_SHORT ).show();
	 SharedPreferences sf=getSharedPreferences("movie", MODE_PRIVATE);
    			 sf.edit().putString("url", url).commit();
    			 Intent i=new Intent(MainActivity.this,MainActivity.class);
    			 startActivity(i);
    			 finish();
    			
    			break;
    		case R.id.tprtd:
    			Toast.makeText(getApplicationContext(), "top rated",Toast.LENGTH_SHORT ).show();
    			SharedPreferences sf1=getSharedPreferences("movie", MODE_PRIVATE);
   			 sf1.edit().putString("url", url_top).commit();
   			Intent i1=new Intent(MainActivity.this,MainActivity.class);
			 startActivity(i1);
			 finish();
    			break;
    		case R.id.upcmg:
    			Toast.makeText(getApplicationContext(), "upcoming",Toast.LENGTH_SHORT ).show();
    			SharedPreferences sf2=getSharedPreferences("movie", MODE_PRIVATE);
      			 sf2.edit().putString("url", url_upcmg).commit();
      			Intent i2=new Intent(MainActivity.this,MainActivity.class);
   			 startActivity(i2);
    			break;
    	default:
    			break;
    	 }
		return super.onOptionsItemSelected(item);
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences sf=getSharedPreferences("movie", MODE_PRIVATE);
        current_url=sf.getString("url", url);
        
        movieList = new ArrayList<HashMap<String, String>>();

        ListView lv= getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// getting values from selected ListItem
				String id1 = ((TextView) view.findViewById(R.id.id))
						.getText().toString();
				String title = ((TextView) view.findViewById(R.id.ttle))
						.getText().toString();
				String releaseDate = ((TextView) view.findViewById(R.id.reldte))
						.getText().toString();

				// Starting details activity
				Intent in = new Intent(getApplicationContext(),
						details.class);
				in.putExtra(TAG_ID,id1);
				startActivity(in);
			}
        	
        
		});
        
		// Calling async task to get json
		new GetMovies().execute();

    }
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	public class GetMovies extends AsyncTask<Void, Void, Void> {

	

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// Creating service handler class instance
						serviceHandler sh = new serviceHandler();

						// Making a request to url and getting response
						String jsonStr = sh.makeServiceCall(current_url, serviceHandler.GET);

						Log.d("Response: ", "> " + jsonStr);

						if (jsonStr != null) {
							try {
								JSONObject jsonObj = new JSONObject(jsonStr);
								
								// Getting JSON Array node
								results = jsonObj.getJSONArray( TAG_RESULTS);

								// looping through All results
								for (int i = 0; i < results.length(); i++) {
									JSONObject c = results.getJSONObject(i);
									
									String iD = c.getString(TAG_ID);
									String title = c.getString(TAG_ORIGINAL_TITLE);
									String releaseDate = sh.reverseDate(c.getString(TAG_RELEASE_DATE));
									//change the date
									String voteAverage = c.getString(TAG_VOTE_AVERAGE);
									String PhoneVoteCount = c.getString(TAG_PHONE_VOTE_COUNT);
									String title2= c.getString(TAG_TITLE);



									// tmp hashmap for single result
									HashMap<String, String> result = new HashMap<String, String>();

									// adding each child node to HashMap key => value
									result.put(TAG_ID, iD);
									result.put(TAG_ORIGINAL_TITLE, title);
									result.put(TAG_RELEASE_DATE,"Release Date: "+releaseDate);
								

									// adding result to movie  list
									movieList.add(result);
								}
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
						 * Updating parsed JSON data into ListView
						 * */
						ListAdapter adapter = new SimpleAdapter(
								MainActivity.this,movieList ,
								R.layout.list_view, new String[] { TAG_ID,TAG_ORIGINAL_TITLE ,
										TAG_RELEASE_DATE }, new int[] { R.id.id,
										R.id.ttle, R.id.reldte });

						setListAdapter(adapter);
		}

	}
}


