package org.zerock.b02.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model){

        log.info("hello......................");

        model.addAttribute("msg", "Hello, World!");
    }
}
