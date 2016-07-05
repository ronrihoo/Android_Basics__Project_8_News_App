package rihoo.newsapp;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FetchNews extends AsyncTask<String, Void, String> {

    // Error Strings
    private String ERROR_CONNECTION_PROBLEM = "Error: there has been a connection problem.";
    private String ERROR_HTTP_STATUS = "Error: received HTTP Status: ";

    // Message Strings
    private String MESSAGE_NO_RESULTS_FOUND = "No results found.";

    // ArrayList<String>
    private ArrayList<String> imgUrl = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> author = new ArrayList<>();
    private ArrayList<String> pageUrl = new ArrayList<>();
    private String status = "";

    // AsyncResponse
    public AsyncResponse delegate = null;

    /**
     * Constructor
     *
     * @param asyncResponse
     */
    public FetchNews(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    /**
     * doInBackground()
     *
     * @param URLs of the news articles
     * @return
     */
    @Override
    protected String doInBackground(String... URLs) {
        StringBuilder strBuilder = new StringBuilder();

        for (String searchURL : URLs) {
            // search URLs
            HttpClient client = new DefaultHttpClient();

            try {
                // get the data
                HttpGet get = new HttpGet(searchURL);

                HttpResponse response = client.execute(get);

                StatusLine searchStatus = response.getStatusLine();

                if (searchStatus.getStatusCode() == 200) {
                    String lineIn;

                    // result
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    InputStreamReader input = new InputStreamReader(content);
                    BufferedReader reader = new BufferedReader(input);

                    while ((lineIn = reader.readLine()) != null) {
                        strBuilder.append(lineIn);
                    }
                } else {
                    strBuilder.append(ERROR_HTTP_STATUS +
                            toString().valueOf(searchStatus.getStatusCode()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (strBuilder.toString() == "") {
                    strBuilder.append(ERROR_CONNECTION_PROBLEM);
                }
            }
        }

        return strBuilder.toString();
    }

    // Parse search results
    protected void onPostExecute(String result) {
        if (result.contains(ERROR_CONNECTION_PROBLEM)) {
            status = ERROR_CONNECTION_PROBLEM;
            delegate.processFinish(status);
        } else if (result.contains(ERROR_HTTP_STATUS)) {
            status = result;
            delegate.processFinish(status);
        } else {
            try {
                JSONObject jsonObject;
                JSONObject responseObject;
                JSONArray resultsArray;
                JSONObject articleObject;
                JSONObject fieldsObject;
                JSONArray tagsArray;
                JSONObject authorObject;

                jsonObject = new JSONObject(result);
                responseObject = jsonObject.getJSONObject("response");
                resultsArray = responseObject.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    articleObject = resultsArray.getJSONObject(i);

                    fieldsObject = articleObject.getJSONObject("fields");
                    tagsArray = articleObject.getJSONArray("tags");

                    // Parse for article thumbnail image URL
                    try {
                        imgUrl.add(fieldsObject.getString("thumbnail"));
                    } catch (JSONException jse) {
                        imgUrl.add("N/A");
                        jse.printStackTrace();
                    }

                    // Parse for the article title
                    try {
                        title.add("Title: " + articleObject.getString("webTitle"));
                    } catch (JSONException jse) {
                        title.add("N/A");
                        jse.printStackTrace();
                    }

                    // Parse for the author(s)
                    StringBuilder authorBuild = new StringBuilder("");

                    try {
                        for (int j = 0; j < tagsArray.length(); j++) {
                            if (j > 0) authorBuild.append(", ");
                            authorObject = tagsArray.getJSONObject(j);
                            authorBuild.append(authorObject.getString("webTitle"));
                        }
                        if (!authorBuild.toString().equals("")) {
                            author.add("Author(s): " + authorBuild.toString());
                        } else {
                            author.add("Author(s): N/A");
                        }
                    } catch (JSONException jse) {
                        author.add("Authors: N/A");
                        jse.printStackTrace();
                    }

                    // Parse for article URL
                    try {
                        pageUrl.add(articleObject.getString("webUrl"));
                    } catch (JSONException jse) {
                        pageUrl.add("NA");
                        jse.printStackTrace();
                    }

                }
                status = "0";
                delegate.processFinish(imgUrl, title, author, pageUrl, status);
            } catch (Exception e) {
                e.printStackTrace();
                status = MESSAGE_NO_RESULTS_FOUND;
                delegate.processFinish(status);
            }
        }
    }

}