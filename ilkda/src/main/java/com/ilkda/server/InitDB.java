package com.ilkda.server;

import com.ilkda.server.book.service.AladinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final AladinService aladinService;

    @PostConstruct
    public void init() {
        aladinService.storeBooks();
    }

}
