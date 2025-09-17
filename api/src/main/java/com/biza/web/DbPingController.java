package com.biza.web;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
public class DbPingController {
    private final JdbcTemplate jdbc;

    public DbPingController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping
    public String ping() {
        Integer one = jdbc.queryForObject("select 1", Integer.class);
        return "ok:" + one;
    }
}
