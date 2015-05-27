package org.sample.elastic.services.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {

    @JsonProperty
    private String id;

    @JsonProperty
    private float score;

    @JsonProperty
    private String source;

    @JsonCreator
    public SearchResult(
        @JsonProperty("id") String id,
        @JsonProperty("score") float score,
        @JsonProperty("source") String source
    ) {
        this.id = id;
        this.score = score;
        this.source = source;
    }

    public String getId(){
        return id;
    }

    public float getScore() {
        return score;
    }

    public String getSource() {
        return source;
    }

}
