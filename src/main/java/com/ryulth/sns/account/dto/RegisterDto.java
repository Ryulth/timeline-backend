package com.ryulth.sns.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class RegisterDto {
    protected RegisterDto(){}
    private String email;
    private String username;
    private String password;
    private String state;
    private String school;
    private String birth;
    @JsonProperty("profileImage")
    private ProfileImageDto profileImageDto;
}
