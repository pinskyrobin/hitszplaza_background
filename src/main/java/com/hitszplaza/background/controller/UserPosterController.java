package com.hitszplaza.background.controller;

import com.hitszplaza.background.service.impl.UserPosterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userPoster")
public class UserPosterController {
    @Autowired
    private UserPosterServiceImpl userPosterService;

    @PostMapping("/findAll")
    public List<Object> findAllUserPoster(@RequestParam(required = false) Integer number) {
        return userPosterService.findAll(number);
    }

    @PostMapping("/updateStatus")
    public String updateUserPosterStatus(@RequestParam String id,
                                     @RequestParam Boolean valid) {
        return userPosterService.updateOneStatus(id, valid);
    }

    @PostMapping("/delete")
    public String deleteUserPoster(@RequestParam String id) {
        return userPosterService.deleteOne(id);
    }

    @PostMapping("/findByOpenId")
    public List<Object> findAllUserPosterByOpenId(@RequestParam(required = false) Integer number,
                                                  @RequestParam String openid) {
        return userPosterService.findAllWithOpenId(number, openid);
    }

}
