package olegari.bio.encurtalink.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import olegari.bio.encurtalink.dto.HistoryResponse;
import olegari.bio.encurtalink.model.UrlMapping;
import olegari.bio.encurtalink.repository.UrlMappingRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    //metodo para contar os clicks
    @Transactional
    public Optional<UrlMapping> getOriginalUrlAndIncrementClick(String shortKey) {
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

    //Metodo para buscar e formatar historico
    public List<HistoryResponse> getHistory(HttpServletRequest request){
        List<UrlMapping> recentMappings = urlMappingRepository.findTop10ByOrderByCreatedAtDesc();
        ;
        //Constroi a Url base para os lings curtos
        String baseUrl = request.getRequestURL().toString();

        return recentMappings.stream()
                .map(mapping -> new HistoryResponse(
                        baseUrl + "/" + mapping.getShortKey(),
                        mapping.getOriginalUrl(),
                        mapping.getClickCount(),
                        mapping.getDaysToExpire()
                        ))
                .collect(Collectors.toList());
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