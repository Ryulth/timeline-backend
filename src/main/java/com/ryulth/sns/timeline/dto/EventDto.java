package com.ryulth.sns.timeline.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryulth.sns.account.dto.ProfileImageDto;
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
    protected EventDto() {
    }

    private Long id;
    private String authorEmail;
    private String authorUsername;
    @JsonProperty("profileImage")
    private ProfileImageDto profileImageDto;
    private String content;
    private String createTime;
    private String updateTime;
    private int hits;
    private int isPublic;
    @JsonProperty("files")
    private List<EventFileDto> files;
}