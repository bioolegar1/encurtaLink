package olegari.bio.encurtalink.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(@NotEmpty @URL String url) {
}
