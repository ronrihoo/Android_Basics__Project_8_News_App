package rihoo.newsapp;

import java.util.ArrayList;


public interface AsyncResponse {
    void processFinish(ArrayList<String> imgUrl,
                       ArrayList<String> title,
                       ArrayList<String> author,
                       ArrayList<String> pageUrl,
                       String status);

    // when nothing has been retrieve, just return status
    void processFinish(String status);
}