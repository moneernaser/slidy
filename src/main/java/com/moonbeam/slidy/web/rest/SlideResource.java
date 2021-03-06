package com.moonbeam.slidy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moonbeam.slidy.config.CloudinaryProperties;
import com.moonbeam.slidy.domain.Slide;

import com.moonbeam.slidy.domain.User;
import com.moonbeam.slidy.repository.SlideRepository;
import com.moonbeam.slidy.repository.UserRepository;
import com.moonbeam.slidy.security.SecurityUtils;
import com.moonbeam.slidy.web.rest.errors.BadRequestAlertException;
import com.moonbeam.slidy.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Slide.
 */
@RestController
@RequestMapping("/api")
public class SlideResource {

    private final Logger log = LoggerFactory.getLogger(SlideResource.class);

    private static final String ENTITY_NAME = "slide";

    private final SlideRepository slideRepository;
    private final UserRepository userRepository;
    private final CloudinaryProperties cloudinaryProperties;

    public SlideResource(SlideRepository slideRepository, UserRepository userRepository, CloudinaryProperties cloudinaryProperties) {
        this.slideRepository = slideRepository;
        this.userRepository = userRepository;
        this.cloudinaryProperties = cloudinaryProperties;
    }

    /**
     * POST  /slides : Create a new slide.
     *
     * @param slide the slide to create
     * @return the ResponseEntity with status 201 (Created) and with body the new slide, or with status 400 (Bad Request) if the slide has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slides")
    @Timed
    public ResponseEntity<Slide> createSlide(@RequestBody Slide slide) throws URISyntaxException, IOException {
        log.debug("REST request to save Slide : {}", slide);
        if (slide.getId() != null) {
            throw new BadRequestAlertException("A new slide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isPresent()) {
            Optional<User> user = userRepository.findOneByLogin(currentUserLogin.get());
            user.ifPresent(slide::setUser);
        }
        Slide result = slideRepository.save(slide);
        return ResponseEntity.created(new URI("/api/slides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    /**
     * PUT  /slides : Updates an existing slide.
     *
     * @param slide the slide to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated slide,
     * or with status 400 (Bad Request) if the slide is not valid,
     * or with status 500 (Internal Server Error) if the slide couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slides")
    @Timed
    public ResponseEntity<Slide> updateSlide(@RequestBody Slide slide) throws URISyntaxException, IOException {
        System.out.println(cloudinaryProperties);;
        log.debug("REST request to update Slide : {}", slide);
        if (slide.getId() == null) {
            return createSlide(slide);
        }
        Slide result = slideRepository.save(slide);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slide.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slides : get all the slides.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of slides in body
     */
    @GetMapping("/slides")
    @Timed
    public List<Slide> getAllSlides() {
        log.debug("REST request to get all Slides");
        return slideRepository.findByUserIsCurrentUser();
        }

    /**
     * GET  /slides/:id : get the "id" slide.
     *
     * @param id the id of the slide to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the slide, or with status 404 (Not Found)
     */
    @GetMapping("/slides/{id}")
    @Timed
    public ResponseEntity<Slide> getSlide(@PathVariable Long id) {
        log.debug("REST request to get Slide : {}", id);
        Slide slide = slideRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(slide));
    }

    /**
     * DELETE  /slides/:id : delete the "id" slide.
     *
     * @param id the id of the slide to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slides/{id}")
    @Timed
    public ResponseEntity<Void> deleteSlide(@PathVariable Long id) {
        log.debug("REST request to delete Slide : {}", id);
        slideRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
