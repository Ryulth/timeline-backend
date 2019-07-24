package com.ryulth.sns.timeline.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryulth.sns.timeline.dto.EventDto;
import com.ryulth.sns.timeline.dto.EventFileDto;
import com.ryulth.sns.timeline.dto.NewEventDto;
import com.ryulth.sns.timeline.entity.Event;
import com.ryulth.sns.timeline.entity.EventFile;
import com.ryulth.sns.timeline.repository.EventRepository;
import com.ryulth.sns.timeline.security.UnauthorizedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EventService {
    public final EventRepository eventRepository;
    public final ObjectMapper objectMapper;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> registerEvent(NewEventDto newEventDto, String authorEmail) {
        List<EventFile> eventFiles = new ArrayList<>();

        //TODO MAPPER 사용하기
        List<EventFileDto> eventFileDtos = newEventDto.getFiles();
        for (EventFileDto eventFileDto : eventFileDtos) {
            eventFiles.add(EventFile.builder()
                    .thumbUrl(eventFileDto.getThumbUrl())
                    .url(eventFileDto.getUrl())
                    .build());
        }

        Event event = Event.builder()
                .authorEmail(authorEmail)
                .content(newEventDto.getContent())
                .eventFiles(eventFiles)
                .build();

        eventRepository.save(event);

        return Collections.singletonMap("success", true);
    }

    public Map<String, Object> getEventById(long eventId, String authorEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        if (event.getIsPublic() == 0 && !event.getAuthorEmail().equals(authorEmail)) {
            throw new UnauthorizedException("Private Event Only Author Can Get Data");
        }

        List<EventFileDto> eventFileDtos = new ArrayList<>();
        for (EventFile eventFile : event.getEventFiles()) {
            eventFileDtos.add(EventFileDto.builder()
                    .thumbUrl(eventFile.getThumbUrl())
                    .url(eventFile.getUrl())
                    .build());
        }


        EventDto eventDto = EventDto.builder()
                .id(event.getId())
                .authorEmail(event.getAuthorEmail())
                .content(event.getContent())
                .createTime(event.getCreateTime())
                .updateTime(event.getUpdateTime())
                .hits(event.getHits())
                .isPublic(event.getIsPublic())
                .files(eventFileDtos)
                .build();

        return Collections.singletonMap("timeline", eventDto);
    }
}
