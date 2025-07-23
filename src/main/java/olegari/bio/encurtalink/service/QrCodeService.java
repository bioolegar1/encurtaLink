package olegari.bio.encurtalink.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLOutput;

@Service
public class QrCodeService {
    /**
     * Gera uma String SVG para um QR Code a partir de um texto.
     * @param text O texto a ser codificado (a nossa URL encurtada).
     * @return Uma string contendo um codigo svg do QR Code.
     */
    public byte[] generateQrCodeImage(String text, int width, int height) {
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix =  qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width,height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        }catch (Exception e) {
            System.out.println("Erro ao gerar imagem do QR CODE: " + e.getMessage());
            return new byte[0];
        }
    }


}
