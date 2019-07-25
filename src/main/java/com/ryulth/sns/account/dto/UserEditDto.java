package com.ryulth.sns.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class UserEditDto {
    protected UserEditDto() {
    }

    private String username;
    private String state;
    private String school;
    private String birth;
    private String imageUrl;
}
