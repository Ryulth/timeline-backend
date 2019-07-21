package com.ryulth.timeline.apis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class FriendRequestDto {
    protected FriendRequestDto(){}
    private String requestEmail;
}
