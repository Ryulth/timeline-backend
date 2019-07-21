package com.ryulth.timeline.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class UserInfoDto {
    private String email;
    private String username;
    private String state;
    private String school;
    private String birth;
}
