package com.shveed.cookmegood.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"id", "avatar", "favouriteList", "ratingList"})
public class User {

    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("countRecipe")
    private int countRecipe;

    @JsonProperty("avgRating")
    private float avgRating;
}
