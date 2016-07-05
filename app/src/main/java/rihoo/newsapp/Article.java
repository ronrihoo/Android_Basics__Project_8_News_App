package rihoo.newsapp;

public class Article {
    // Constants
    private static final String NO_THUMBNAIL = "NA";

    // Variables
    private String imgUrl;
    private String title;
    private String author;
    private String pageUrl;

    /**
     * Constructor
     *
     * @param imgUrl  of the article
     * @param title   of the article
     * @param author  of the article
     * @param pageUrl of the article
     */
    public Article(String imgUrl, String title, String author, String pageUrl) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.author = author;
        this.pageUrl = pageUrl;
    }

    /**
     * Get the article's image URL.
     */
    public String getImgUrl() {
        return this.imgUrl;
    }

    /**
     * Get the article's title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the author's name.
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Get the article's page URL
     */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * Check whether the article has a thumbnail URL.
     */
    public boolean hasImage() {
        return imgUrl != NO_THUMBNAIL;
    }
}
