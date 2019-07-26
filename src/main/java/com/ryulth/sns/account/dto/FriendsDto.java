package com.ryulth.sns.account.dto;

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
public class FriendsDto {
    protected FriendsDto(){
    }
    @JsonProperty("friends")
    private List<FriendInfoDto> friendInfoDtos;
}
