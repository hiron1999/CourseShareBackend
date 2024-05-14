package com.courseshare.UserService.service;

import java.util.List;

import Model.UserModel;
import com.courseshare.UserService.entity.UserEntity;

public interface UserSrevice {
    public List<UserModel> getAll();
    public UserModel getById(long id);
//    public UserModel addUser(UserModel user);
    public UserModel getByEmail(String email);
}
