package br.com.ether.utilities;


import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogUtility {
    private static final Logger log = LogManager.getLogger("ETHER");

    public void registraLog(String mensagem) {
        log.info(mensagem);
    }

    public void registraErro(String mensagem) {
        log.error(mensagem);
    }

    public void registraException(String mensagem, Exception e) {
        log.error(mensagem, e);
    }

}
