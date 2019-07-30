package com.ryulth.sns.timeline.service;

import com.ryulth.sns.account.dto.ProfileImageDto;
import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import com.ryulth.sns.config.UnauthorizedException;
import com.ryulth.sns.timeline.dto.EventDto;
import com.ryulth.sns.timeline.dto.EventFileDto;
import com.ryulth.sns.timeline.dto.NewEventDto;
import com.ryulth.sns.timeline.entity.Event;
import com.ryulth.sns.timeline.entity.EventFile;
import com.ryulth.sns.timeline.repository.EventFileRepository;
import com.ryulth.sns.timeline.repository.EventRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventFileRepository eventFileRepository;
    private final UserRepository userRepository;
    private final SimpleDateFormat simpleDateFormat;
    public EventService(EventRepository eventRepository, EventFileRepository eventFileRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventFileRepository = eventFileRepository;
        this.userRepository = userRepository;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    public EventDto registerEvent(NewEventDto newEventDto, String authorEmail) {
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

        return getEventDtoByEvent(event);
    }

    public EventDto getEvent(long eventId, String accessEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        if (event.getIsPublic() == 0 && !event.getAuthorEmail().equals(accessEmail)) {
            throw new UnauthorizedException("Private Event Only Author Can Get Data");
        }
        return getEventDtoByEvent(event);
    }

    public EventDto deleteEvent(long eventId, String accessEmail){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        if(accessEmail.equals(event.getAuthorEmail())){
            eventRepository.deleteById(eventId);
            eventFileRepository.deleteAllByEventId(eventId);
            return getEventDtoByEvent(event);
        }
        throw new UnauthorizedException("NOT AUTHOR EMAIL");
    }
    EventDto getEventDtoByEvent(Event event){
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
                .profileImageDto(ProfileImageDto.builder()
                        .url(user.getImageUrl())
                        .thumbUrl(user.getThumbImageUrl())
                        .build())
                .id(event.getId())
                .authorEmail(event.getAuthorEmail())
                .authorUsername(user.getUsername())
                .content(event.getContent())
                .createTime(simpleDateFormat.format(event.getCreateTime().getTime()))
                .updateTime(simpleDateFormat.format(event.getUpdateTime().getTime()))
                .hits(event.getHits())
                .isPublic(event.getIsPublic())
                .files(eventFileDtos)
                .build();

        return eventDto;
    }

}
