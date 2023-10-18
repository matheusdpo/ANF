package br.com.ether.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitMoment {
    public void miliSeconds(int tempo) {
        try {
            Thread.sleep(tempo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void seconds(int tempo) {
        try {
            Thread.sleep(tempo * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minutes(int tempo) {
        try {
            Thread.sleep(tempo * 1000 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hours(int tempo) {
        try {
            Thread.sleep(tempo * 1000 * 60 * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
