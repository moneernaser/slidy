package com.moonbeam.slidy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.moonbeam.slidy.config.SecurityConfiguration;
import com.moonbeam.slidy.domain.PersistentToken;
import com.moonbeam.slidy.domain.Slide;

import com.moonbeam.slidy.domain.User;
import com.moonbeam.slidy.repository.SlideRepository;
import com.moonbeam.slidy.repository.UserRepository;
import com.moonbeam.slidy.security.SecurityUtils;
import com.moonbeam.slidy.service.util.RandomUtil;
import com.moonbeam.slidy.web.rest.errors.BadRequestAlertException;
import com.moonbeam.slidy.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.*;
import java.util.*;

/**
 * REST controller for managing Slide.
 */
@RestController
@RequestMapping("/slideshow")
public class SlideshowResource {



    private final SlideRepository slideRepository;
    private final UserRepository userRepository;

    public SlideshowResource(SlideRepository slideRepository, UserRepository userRepository) {
        this.slideRepository = slideRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/")
    public RedirectView redirectToSlideshowPage(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        System.out.println(attributes.asMap().get("user"));
        String username = SecurityUtils.getCurrentUserLogin().orElse((String) attributes.asMap().get("user"));
        attributes.addAttribute("user", username);
        return new RedirectView("/slideshow.html");
    }


    @GetMapping("/get")
    public List<Slide> getSlidesForUser(@RequestParam("user") String userlogin,
                                        @RequestParam(value = "lastCheck", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date lastCheck,
                                        @RequestParam(value = "count", required = false) Integer count) {
        return userRepository.findOneByLogin(userlogin).map(user -> {

            // if no previous fetch
            if (lastCheck == null) {
                return slideRepository.findByUser(user);
            }

            // else: check if resources Updated since lastCheck
            boolean resourceUpdated = slideRepository.existsByLastModifiedDateAfter(lastCheck.toInstant());
            if (resourceUpdated) {
                return slideRepository.findByUser(user);
            }

            // check if some resources were Deleted (by comparing with user-specified count)
            Long userSlidesCount = slideRepository.countByUser(user);
            if (count == null || userSlidesCount.intValue() != count) {
                return slideRepository.findByUser(user);
            }

            return null;
        }).orElse(null);
    }

}
