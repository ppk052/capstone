package com.example.capstone.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @GetMapping("/")
    public String dashboard() {

        return null;
    }
}
