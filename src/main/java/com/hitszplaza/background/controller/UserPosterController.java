package com.hitszplaza.background.controller;

import com.hitszplaza.background.service.impl.UserPosterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userPoster")
public class UserPosterController {
    @Autowired
    private UserPosterServiceImpl userPosterService;

    @PostMapping("/update")
    public String updatePosterStatus(@RequestParam String id,
                                     @RequestParam Boolean valid) {
        return userPosterService.updateStatus(id, valid);
    }
}
