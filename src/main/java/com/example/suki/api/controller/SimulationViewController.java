package com.example.suki.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimulationViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/reach")
    public String reach() {
        return "reach";
    }

    @GetMapping("/finish-at")
    public String finishAt() {
        return "finish-at";
    }

    @GetMapping("/finish-within")
    public String finishWithin() {
        return "finish-within";
    }
}
