package org.tizzer.smmgr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/smmgr")
public class LocationController {

    /**
     * 返回定位网页
     *
     * @return
     */
    @GetMapping(path = "/location")
    public String location() {
        return "map";
    }

}
