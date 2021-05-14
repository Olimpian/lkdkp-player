package biz.eurosib.lkdkp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, i'm player!";
    }

    @GetMapping("/version")
    public String version() {
        //todo release version
        return "1.0";
    }
}
