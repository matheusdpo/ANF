package br.com.ether.utilities;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ConverterUtility {

    public String encondeBase64(String value) {
        byte[] pdfBytes = null;

        try {
            pdfBytes = Files.readAllBytes(Path.of(value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeBase64String(pdfBytes);
    }
}
