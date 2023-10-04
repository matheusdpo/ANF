package br.com.ether.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Aguardar {
    public void miliSegundos(int tempo) {
        try {
            Thread.sleep(tempo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void segundos(int tempo) {
        try {
            Thread.sleep(tempo * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minutos(int tempo) {
        try {
            Thread.sleep(tempo * 1000 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void horas(int tempo) {
        try {
            Thread.sleep(tempo * 1000 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
