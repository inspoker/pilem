package com.belajar.posma.retrofitposma.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Posma on 9/22/2017.
 */

public class CreditsResponse {

    private List<Credits> cast;
    public List<Credits> getCast() {
        return cast;
    }
    public void setCast(List<Credits> cast) {
        this.cast = cast;
    }
}


