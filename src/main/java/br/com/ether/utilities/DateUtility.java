package br.com.ether.utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DateUtility {

    public String getToday(String format) {
        //retorna dia de hoje
        return new SimpleDateFormat(format).format(new Date());
    }


}
