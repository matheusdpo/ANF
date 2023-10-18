package br.com.ether.anf.controller;


import br.com.ether.anf.repository.DataBase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@EnableScheduling
public class DatabaseController {
    private final DataBase dataBase;

    @PostConstruct
    @Scheduled(cron = "0 0 * * * *")
    public void init() {
        dataBase.connectDB();
    }
}