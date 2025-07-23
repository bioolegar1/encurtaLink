package olegari.bio.encurtalink.repository;

import olegari.bio.encurtalink.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);

    Optional<UrlMapping> findByShortKey(String shortKey);

    List<UrlMapping> findTop10ByCreatedAtDesc();
}
