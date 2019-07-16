package com.ryulth.timeline.apis.entity;

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

    @Column(name = "event_file_name")
    private String name;

    @Column(name = "event_file_src")
    private String src;

}
