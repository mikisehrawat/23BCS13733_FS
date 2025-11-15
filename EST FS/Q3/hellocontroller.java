package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class hellocontroller {

    // GET request
    @GetMapping("/hello")
    public String sayHello(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            name = "Guest";
        }
        return "Hello, " + name;
    }

    @PostMapping("/save")
    public String saveData(
            @RequestParam String name,
            @RequestParam int age
    ) {
        return "Received -> Name: " + name + ", Age: " + age;
    }
}
