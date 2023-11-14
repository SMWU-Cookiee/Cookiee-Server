
package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByMonth(int month);
}
