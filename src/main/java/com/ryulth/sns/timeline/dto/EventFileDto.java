package com.ryulth.sns.timeline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class EventFileDto {
    protected EventFileDto(){}
    private String thumbUrl;
    private String url;
}
