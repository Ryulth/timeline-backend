package com.ryulth.sns.timeline.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "event",indexes = {@Index(columnList = "event_author_email")})
@ToString
public class Event {
    protected Event() {
        eventFiles = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_author_email",nullable = false)
    private String authorEmail = "";

    @Lob
    @Column(name = "event_content" ,nullable = false)
    private String content = "";

    @Column(name = "event_create_time", nullable = false, updatable = false)
    private Calendar createTime;

    @Column(name = "event_update_time")
    private Calendar updateTime;

    @Column(name = "event_hits")
    private int hits;

    @Column(name = "event_is_public", columnDefinition = "TINYINT(1)")
    private int isPublic;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<EventFile> eventFiles;

    @PrePersist
    void setUp() {
        this.hits = 0;
        this.isPublic = 1;
        this.createTime = Calendar.getInstance();
        this.updateTime = Calendar.getInstance();
    }
}
