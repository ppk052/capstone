package com.example.capstone.Controller;

import com.example.capstone.Entity.Plant;
import com.example.capstone.Entity.PlantAppropriateValue;
import com.example.capstone.Service.PlantService;
import com.example.capstone.Service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashBoardController {


    //각종제어장치넣어야됨
    @GetMapping("/")
    public String dashboard(Model model) {
        return "dashboard";
    }
}
