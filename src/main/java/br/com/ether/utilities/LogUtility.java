package br.com.ether.utilities;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogUtility {
    private static final Logger log = LogManager.getLogger("ETHER");

    public void registerLog(String mensagem) {
        log.info(mensagem);
    }

    public void registerError(String mensagem) {
        log.error(mensagem);
    }

    public void registerException(String mensagem, Exception e) {
        log.error(mensagem, e);
    }

}
