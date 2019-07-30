package com.ryulth.sns.timeline.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "event_file")
@ToString
public class EventFile {
    protected EventFile() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_file_id")
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Lob
    @Column(name = "event_image_thumbUrl",nullable = false)
    private String thumbUrl;

    @Lob
    @Column(name = "event_image_url",nullable = false)
    private String url;

}
