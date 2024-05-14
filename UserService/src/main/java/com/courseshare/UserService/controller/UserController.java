package com.courseshare.UserService.controller;

import java.util.List;

import Model.UserModel;
import com.courseshare.UserService.service.UserSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Profile")
public class UserController {

    @Autowired
    private UserSrevice userSrevice;
    @GetMapping("/user/{id}")
    public ResponseEntity<UserModel> getUser(@PathVariable(name = "id") long id) {

        return new ResponseEntity<UserModel>(userSrevice.getById(id), HttpStatus.OK);
    }
    @GetMapping("users")
    public List<UserModel> getAllUsers(){
        return userSrevice.getAll();
    }
//    @PostMapping("/create")
//    public UserModel createUser(@RequestBody UserModel user){
//        return userSrevice.addUser(user);
//    }
}
