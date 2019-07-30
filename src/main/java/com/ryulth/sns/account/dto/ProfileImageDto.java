package com.ryulth.sns.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ProfileImageDto {
    protected ProfileImageDto(){}
    private String thumbUrl;
    private String url;
}
