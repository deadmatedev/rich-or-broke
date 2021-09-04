package com.deadmate.richorbroke.model.giphy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiphySearchResult {
    private String id;
    private GiphyImages images;
}
