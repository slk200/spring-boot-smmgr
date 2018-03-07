package org.tizzer.smmgr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/smmgr")
public class LocationController {

    @GetMapping("/location")
    public String location() {
        return "map";
    }

}
