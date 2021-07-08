package com.hitszplaza.background.controller;

import com.hitszplaza.background.service.impl.UserPosterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/userPoster")
public class UserPosterController {
    @Autowired
    private UserPosterServiceImpl userPosterService;

    @PostMapping("/findAll")
    public ArrayList<String> findAllUserPoster(@RequestParam Integer number) {
        return userPosterService.findAll(number);
    }

    @PostMapping("/update")
    public String updateUserPosterStatus(@RequestParam String id,
                                     @RequestParam Boolean valid) {
        return userPosterService.updateOneStatus(id, valid);
    }
}
