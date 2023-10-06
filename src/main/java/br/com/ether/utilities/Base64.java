package br.com.ether.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Base64 {

    public String encode(String value) {
        return java.util.Base64.getEncoder().encodeToString(value.getBytes());
    }

    public String decode(String value) {
        return new String(java.util.Base64.getDecoder().decode(value));
    }
}
