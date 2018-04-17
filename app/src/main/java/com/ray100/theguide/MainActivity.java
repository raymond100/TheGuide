package com.ray100.theguide;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.id.list;

/**
 * Displays information about a single institution.
 */
public class MainActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** URL to query the USGS dataset for institution information */
    private static final String GUIDE_REQUEST_URL =
            "http://www.innov-haiti.org/leguide/guide.php";

    private Guide guide;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kick off an {@link AsyncTask} to perform the network request
        GuideAsyncTask task = new GuideAsyncTask();
        task.execute();
    }

    /**
     * Update the screen to display information from the given {@link Guide}.
     */
    private void updateUi(final ArrayList<Guide> institution) {

        GuideAdapter adapter = new GuideAdapter(this, institution);

       listView = (ListView) findViewById(R.id.guide_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, GuideDesc.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);

    }



    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first institution in the response.
     */
     class GuideAsyncTask extends AsyncTask<URL, Void, ArrayList<Guide>> {

        @Override
        protected ArrayList<Guide> doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(GUIDE_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            // Extract relevant fields from the JSON response and create an {@link Guide} object
            ArrayList<Guide> institution = extractFeatureFromJson(jsonResponse);

            // Return the {@link Guide} object as the result fo the {@link GuideAsyncTask}
            return institution;
        }

        /**
         * Update the screen with the given institution (which was the result of the
         * {@link GuideAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<Guide> institution) {
            if (institution == null) {
                return;
            }

            updateUi(institution);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link Guide} object by parsing out information
         * about the first institution from the input institutionJSON string.
         */
        private ArrayList<Guide> extractFeatureFromJson(String institutionJSON) {
            ArrayList<Guide> guideArray = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(institutionJSON);
                JSONArray guideJsonArray = baseJsonResponse.getJSONArray("result");

                // If there are results in the features array
                for (int i = 0; i < guideJsonArray.length(); i++)
                {
                    JSONObject currentGuide = guideJsonArray.getJSONObject(i);
                    String name = currentGuide.getString("name");
                    String address = currentGuide.getString("adress");
                    String image = currentGuide.getString("image");

                    guide =  new Guide(name,address,image);
                    guideArray.add(guide);

               }


            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the institution JSON results", e);
            }
            return guideArray;
        }
    }
}