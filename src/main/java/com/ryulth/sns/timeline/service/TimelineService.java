package com.ryulth.sns.timeline.service;

import com.ryulth.sns.timeline.entity.Event;
import com.ryulth.sns.timeline.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TimelineService {
    private final EventRepository eventRepository;

    public TimelineService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Map<String, Object> getTimeline(String accessEmail,String authorEmail,int page){
        Pageable pageable = PageRequest.of(page,5,Sort.by("id").descending());
        List<Event> events = eventRepository.findByAuthorEmail(authorEmail,pageable);
        if(!accessEmail.equals(authorEmail)){
            events.removeIf(event -> event.getIsPublic() == 0);
        }
        return Collections.singletonMap("events",events);
    }
}
