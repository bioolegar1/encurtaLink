package olegari.bio.encurtalink.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import olegari.bio.encurtalink.dto.ShortenUrlRequest;
import olegari.bio.encurtalink.dto.ShortenUrlResponse;
import olegari.bio.encurtalink.model.UrlMapping;
import olegari.bio.encurtalink.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request, HttpServletRequest servletRequest) {
        UrlMapping urlMapping = urlShortenerService.shortenUrl(request.url());

        String shortUrl = servletRequest.getScheme() + "://" + servletRequest.getServerName() +
                ":" + servletRequest.getServerPort() + servletRequest.getContextPath() +
                "/" + urlMapping.getShortKey();

        return ResponseEntity.ok(new ShortenUrlResponse(shortUrl));
    }

    @GetMapping("/{shortKey:[a-zA-Z0-9]{5,10}}")
    public void redirectToOriginalUrl(@PathVariable String shortKey, HttpServletResponse response) throws IOException {
        Optional<UrlMapping> urlMappingOptional = urlShortenerService.getOriginalUrl(shortKey);

        if (urlMappingOptional.isPresent()) {
            response.sendRedirect(urlMappingOptional.get().getOriginalUrl());
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "URL not found or expired");
        }
    }
}