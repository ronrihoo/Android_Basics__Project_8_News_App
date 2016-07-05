package rihoo.newsapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ArticleAdapter extends ArrayAdapter<Article> {

    private int colorResourceId;

    /**
     * Constructor
     */
    public ArticleAdapter(Context context, int colorResourceId, ArrayList<Article> articles) {
        super(context, 0, articles);
        this.colorResourceId = colorResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the color for the background
        int color = ContextCompat.getColor(getContext(), this.colorResourceId);

        // convertView
        View listItemView = convertView;

        // Check whether convertView is null
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_news_item, parent, false);
        }

        // View/TextViews
        View textContainer = listItemView.findViewById(R.id.LL_Text_Container);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.TV_Title);
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.TV_Author);
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.ImageView);

        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);

        // Article object
        Article currentArticle = getItem(position);

        // Look for image. If it exists, then display it. Else, hide the ImageView.
        if (currentArticle.hasImage()) {
            Picasso.with(getContext()).load(currentArticle.getImgUrl()).into(imageView);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        // Display object's information in ListView item
        titleTextView.setText(currentArticle.getTitle());
        authorTextView.setText(currentArticle.getAuthor());

        return listItemView;
    }

}
