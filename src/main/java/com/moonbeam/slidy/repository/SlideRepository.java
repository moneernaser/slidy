package com.moonbeam.slidy.repository;

import com.moonbeam.slidy.domain.Slide;
import com.moonbeam.slidy.domain.User;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the Slide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlideRepository extends JpaRepository<Slide, Long> {

    @Query("select slide from Slide slide where slide.user.login = ?#{principal.username}")
    List<Slide> findByUserIsCurrentUser();

    List<Slide> findByUser(User user);

    boolean existsByLastModifiedDateAfter(Instant lastCheck);


    Long countByUser(User user);
}
