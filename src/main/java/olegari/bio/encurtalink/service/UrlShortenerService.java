package olegari.bio.encurtalink.service;

import olegari.bio.encurtalink.model.UrlMapping;
import olegari.bio.encurtalink.repository.UrlMappingRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;


    private static final int SHORT_KEY_LENGTH = 7;
    private static final long EXPIRATION_DAYS = 30; // URL expira em 30 dias

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlMapping shortenUrl(String originalUrl) {
        String shortKey = generateUniqueShortKey();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(EXPIRATION_DAYS);

        UrlMapping urlMapping = new UrlMapping(originalUrl, shortKey, now, expiresAt);
        return urlMappingRepository.save(urlMapping);
    }

    public Optional<UrlMapping> getOriginalUrl(String shortKey) {
        Optional<UrlMapping> urlMappingOptional = urlMappingRepository.findByShortKey(shortKey);

        if (urlMappingOptional.isPresent()) {
            UrlMapping urlMapping = urlMappingOptional.get();
            if (urlMapping.isExpired()) {
                urlMappingRepository.delete(urlMapping);
                return Optional.empty();
            }
        }
        return urlMappingOptional;
    }

    private String generateUniqueShortKey() {
        String shortKey;
        do {
            // Usando um tamanho de chave fixo.
            shortKey = RandomStringUtils.randomAlphanumeric(SHORT_KEY_LENGTH);
        } while (urlMappingRepository.findByShortKey(shortKey).isPresent());
        return shortKey;
    }
}