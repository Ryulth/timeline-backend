package com.ryulth.timeline.apis.service;

import com.ryulth.timeline.apis.dto.EventFileDto;
import com.ryulth.timeline.apis.dto.NewEventDto;
import com.ryulth.timeline.apis.entity.Event;
import com.ryulth.timeline.apis.entity.EventFile;
import com.ryulth.timeline.apis.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EventService {
    public final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Map<String, Object> registerEvent(NewEventDto newEventDto,String authorEmail) {
        List<EventFile> eventFiles = new ArrayList<>();

        //TODO MAPPER 사용하기
        List<EventFileDto> eventFileDtos = newEventDto.getFiles();
        for (EventFileDto eventFileDto : eventFileDtos) {
            eventFiles.add(EventFile.builder()
                    .name(eventFileDto.getName())
                    .src(eventFileDto.getSrc())
                    .build());
        }

        Event event = Event.builder()
                .authorEmail(authorEmail)
                .title(newEventDto.getTitle())
                .content(newEventDto.getContent())
                .eventFiles(eventFiles)
                .build();

        eventRepository.save(event);

        return Collections.singletonMap("success", true);
    }

}
