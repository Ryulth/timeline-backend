package com.ryulth.timeline.apis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryulth.timeline.apis.entity.RelationshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class FriendInfoDto {
    protected FriendInfoDto(){}
    private String email;
    private String username;
    private String state;
    private String school;
    private String birth;
    @JsonProperty("relationship")
    private RelationshipStatus relationshipStatus;
}