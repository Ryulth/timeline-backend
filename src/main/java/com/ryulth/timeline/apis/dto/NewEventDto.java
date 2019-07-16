package com.ryulth.timeline.apis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class NewEventDto {
    private String title;
    private String content;
    @JsonProperty("files")
    private List<EventFileDto> files;
}