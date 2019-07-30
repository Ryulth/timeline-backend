package com.ryulth.sns.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user")
@ToString
public class User {
    protected User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email",unique = true ,nullable = false)
    private String email;

    @Column(name = "user_name",nullable = false)
    private String username ;

    @Column(name = "user_password",nullable = false)
    private String password ;

    @Column(name = "user_state" ,nullable = false)
    private String state ;

    @Column(name = "user_school" ,nullable = false)
    private String school = "";

    @Column(name = "user_birth" ,nullable = false)
    private String birth;

    @Lob
    @Column(name = "user_thumb_image_url",nullable = false)
    private String thumbImageUrl = "";

    @Lob
    @Column(name = "user_image_url" , nullable = false)
    private String imageUrl = "";
}
