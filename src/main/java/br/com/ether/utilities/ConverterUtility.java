package br.com.ether.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ConverterUtility {

    public String encondeBase64(String inputfile) {

        try {
            // Lê o arquivo PDF
            File pdfFile = new File(inputfile);
            byte[] pdfBytes = null;
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            pdfBytes = new byte[(int) pdfFile.length()];
            fileInputStream.read(pdfBytes);
            return Base64.getEncoder().encodeToString(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao converter PDF para Base64";
        }
    }
}
