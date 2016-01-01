package me.takus.hotmovie.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class Movie implements Serializable {
    private String title;
    private String overview;
    private String posterUrl;
    private String releaseDate;
    private double popularity;
    private double voteAverage;
}