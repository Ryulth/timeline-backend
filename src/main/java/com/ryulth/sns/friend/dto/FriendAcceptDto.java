package com.ryulth.sns.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class FriendAcceptDto {
    protected FriendAcceptDto(){}
    private boolean accept;
}
