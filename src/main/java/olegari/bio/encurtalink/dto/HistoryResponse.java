package olegari.bio.encurtalink.dto;

public record HistoryResponse(
        String shortUrl,
        String originalUrl,
        long clickCount,
        long daysToExpire,
        String qrCodeUrl
) {}
