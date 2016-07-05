package rihoo.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    // ArrayList<Article>
    ArrayList<Article> articles;

    // ArticleAdapter
    ArticleAdapter articleAdapter;

    // ListView
    ListView listView;

    // AsyncTask
    FetchNews fetchNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Articles
        articles = new ArrayList<Article>();

        // ListView
        listView = (ListView) findViewById(R.id.listView);

        // Grab the news
        grabNews();

        // refresh for updates every 30 seconds
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                grabNews();
            }
        }, 0, 30000);
        /**/

    }

    public void grabNews() {
        String url = createQueryString("stocks");

        // ArticleAdapter
        articleAdapter = new ArticleAdapter(this, R.color.listview_item_color, articles);

        // AsyncTask
        fetchNews = new FetchNews(new AsyncResponse() {

            @Override
            public void processFinish(ArrayList<String> imgUrl,
                                      ArrayList<String> title,
                                      ArrayList<String> author,
                                      ArrayList<String> pageUrl,
                                      String status) {

                // if there are articles in the list, then clear them out for the new run
                if (articles.size() > 0) {
                    articles.clear();
                }

                for (int i = 0; i < title.size(); i++) {
                    articles.add(new Article(imgUrl.get(i), title.get(i), author.get(i),
                            pageUrl.get(i)));
                }

                listView.setAdapter(articleAdapter);
            }

            // When there are issues and nothing is retrieved
            public void processFinish(String status) {
                handleMessage(status);
            }

        });
        fetchNews.execute(url);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(articles.get(position).getPageUrl())));
            }
        });

    }

    // This function can help support user searches in a future version
    public String createQueryString(String topic) {
        return "http://content.guardianapis.com/search?" +
                "show-fields=thumbnail" +
                "&show-tags=contributor" +
                "&q=" + topic +
                "&api-key=test";
    }

    public void handleMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

}