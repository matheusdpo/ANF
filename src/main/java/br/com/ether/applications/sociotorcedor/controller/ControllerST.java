package br.com.ether.applications.sociotorcedor.controller;

import br.com.ether.applications.sociotorcedor.services.SocioTorcedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@EnableScheduling
@RequiredArgsConstructor
public class ControllerST {

    private final SocioTorcedorService socioTorcedorService;

    @Scheduled(cron = "0 0 5 * * *")
    public void init() {
        socioTorcedorService.init();
    }
}
