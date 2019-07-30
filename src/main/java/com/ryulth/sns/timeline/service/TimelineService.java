package com.ryulth.sns.timeline.service;

import com.ryulth.sns.account.dto.FriendInfoDto;
import com.ryulth.sns.account.dto.FriendsDto;
import com.ryulth.sns.account.service.friend.FriendService;
import com.ryulth.sns.timeline.dto.EventDto;
import com.ryulth.sns.timeline.dto.TimelineDto;
import com.ryulth.sns.timeline.entity.Event;
import com.ryulth.sns.timeline.repository.EventRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimelineService {
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final FriendService friendService;

    public TimelineService(EventRepository eventRepository, EventService eventService, FriendService friendService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.friendService = friendService;
    }

    public TimelineDto getUserTimeline(String accessEmail, String authorEmail, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        List<Event> events = eventRepository.findByAuthorEmail(authorEmail, pageable);

        if (!accessEmail.equals(authorEmail)) {
            events.removeIf(event -> event.getIsPublic() == 0);
        }
        return TimelineDto.builder().eventDtos(eventToEventDto(events)).build();
    }

    public TimelineDto getTimeline(String accessEmail, int page) {
        FriendsDto friendsDto = friendService.getFriends(accessEmail);
        List<String> authorEmails = new ArrayList<>();
        authorEmails.add(accessEmail);
        for (FriendInfoDto friendInfoDto : friendsDto.getFriendInfoDtos()) {
            authorEmails.add(friendInfoDto.getEmail());
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        List<Event> events = eventRepository.findByAuthorEmailIn(authorEmails, pageable);

        events.removeIf(event -> !event.getAuthorEmail().equals(accessEmail) && event.getIsPublic() == 0);
        return TimelineDto.builder().eventDtos(eventToEventDto(events)).build();
    }

    private List<EventDto> eventToEventDto(List<Event> events) {
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(eventService.getEventDtoByEvent(event));
        }
        return eventDtos;
    }
}
