package com.journal.journal_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/journal")
public class JournalController {
    @RequestMapping("/testString")
    public String getTestString() {

        return "TEST STRING SENT";
    }
}
