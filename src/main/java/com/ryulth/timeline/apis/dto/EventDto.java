package com.ryulth.timeline.apis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Calendar;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class EventDto {
    private Long id;
    private String authorEmail;
    private String title;
    private String content;
    private Calendar createTime;
    private Calendar updateTime;
    private int hits;
    private int isPublic;
    @JsonProperty("files")
    private List<EventFileDto> files;
}