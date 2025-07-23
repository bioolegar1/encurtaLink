package olegari.bio.encurtalink.controller;

import ch.qos.logback.core.net.server.ServerListener;
import jakarta.servlet.Servlet;
import olegari.bio.encurtalink.service.QrCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/qrcode/{shortKey}")
    //reconstroi a URL encurtda completa para codificar o QRCode
    public ResponseEntity<byte[]> getQrCode(@PathVariable String shortKey) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String shortUrl = baseUrl + "/" + shortKey;

        //Ger a imagem do QR code com um array de bytes
        byte[] qrCodeImage = qrCodeService.generateQrCodeImage(shortUrl, 250,250);

        if (qrCodeImage.length == 0) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
                .body(qrCodeImage);


    }

}
