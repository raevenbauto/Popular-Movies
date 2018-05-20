package com.example.raeven.popularmovies.Model;

import java.io.Serializable;

public class MovieModel implements Serializable{

    private int id;
    private String voteAverage;
    private String title;
    private String mainPosterLink;
    private String originalTitle;
    private String backPosterLink;
    private String overview;
    private String releaseDate;

    public MovieModel(int id, String voteAverage, String title, String mainPosterLink, String originalTitle,
                        String backPosterLink, String overview, String releaseDate){
        this.id = id;
        this.title = title;
        this.mainPosterLink = mainPosterLink;
        this.originalTitle = originalTitle;
        this.backPosterLink = backPosterLink;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainPosterLink() {
        return mainPosterLink;
    }

    public void setMainPosterLink(String mainPosterLink) {
        this.mainPosterLink = mainPosterLink;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackPosterLink() {
        return backPosterLink;
    }

    public void setBackPosterLink(String backPosterLink) {
        this.backPosterLink = backPosterLink;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
