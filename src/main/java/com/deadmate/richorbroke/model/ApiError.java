package com.deadmate.richorbroke.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {
    private int status;
    private String title;
    private String details;
}
