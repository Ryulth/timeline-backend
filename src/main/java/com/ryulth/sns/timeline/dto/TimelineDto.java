package com.ryulth.sns.timeline.dto;

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
public class TimelineDto {
    protected TimelineDto(){
    }
    @JsonProperty("events")
    private List<EventDto> eventDtos;
}
