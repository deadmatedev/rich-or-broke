package com.deadmate.richorbroke.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Image {
    private final String id;
    private final String url;
    private final long size;
    private final String hash;
}
