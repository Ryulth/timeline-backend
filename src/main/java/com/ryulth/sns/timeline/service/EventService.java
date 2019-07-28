package com.ryulth.sns.timeline.service;

import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import com.ryulth.sns.timeline.dto.EventDto;
import com.ryulth.sns.timeline.dto.EventFileDto;
import com.ryulth.sns.timeline.dto.NewEventDto;
import com.ryulth.sns.timeline.entity.Event;
import com.ryulth.sns.timeline.entity.EventFile;
import com.ryulth.sns.timeline.repository.EventFileRepository;
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
    private final EventRepository eventRepository;
    private final EventFileRepository eventFileRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, EventFileRepository eventFileRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventFileRepository = eventFileRepository;
        this.userRepository = userRepository;
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

    public Map<String, Object> getEvent(long eventId, String accessEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        if (event.getIsPublic() == 0 && !event.getAuthorEmail().equals(accessEmail)) {
            throw new UnauthorizedException("Private Event Only Author Can Get Data");
        }
        EventDto eventDto = getEventDtoByEvent(event);


        return Collections.singletonMap("timeline", eventDto);
    }

    public EventDto getEventDtoByEvent(Event event){
        List<EventFileDto> eventFileDtos = new ArrayList<>();
        for (EventFile eventFile : event.getEventFiles()) {
            eventFileDtos.add(EventFileDto.builder()
                    .thumbUrl(eventFile.getThumbUrl())
                    .url(eventFile.getUrl())
                    .build());
        }
        User user = userRepository.findByEmail(event.getAuthorEmail())
                .orElseThrow(EntityNotFoundException::new);

        EventDto eventDto = EventDto.builder()
                .id(event.getId())
                .authorEmail(event.getAuthorEmail())
                .authorUsername(user.getUsername())
                .content(event.getContent())
                .createTime(event.getCreateTime())
                .updateTime(event.getUpdateTime())
                .hits(event.getHits())
                .isPublic(event.getIsPublic())
                .files(eventFileDtos)
                .build();

        return eventDto;
    }

    public Map<String, Object> deleteEvent(long eventId, String accessEmail){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        if(accessEmail.equals(event.getAuthorEmail())){
            eventRepository.deleteById(eventId);
            eventFileRepository.deleteAllByEventId(eventId);
            return Collections.singletonMap("delete",true);
        }
        throw new UnauthorizedException("NOT AUTHOR EMAIL");
    }
}
