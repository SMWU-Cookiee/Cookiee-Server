
package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUserUserIdAndEventYearAndEventMonthAndEventDate(Long userId, int evntYear, int eventMonth, int eventDate);

    Event findByUserUserIdAndEventId(Long userId, Long eventId);
}


