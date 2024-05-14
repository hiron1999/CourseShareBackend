package com.courseshare.UserService.Utils;

import Model.UserModel;
import com.courseshare.UserService.entity.UserEntity;

public class UserUtils {
    public static UserModel toModel(UserEntity entity){
        UserModel model =new UserModel();

        model.setUserid(entity.getId());
        model.setFirst_name(entity.getFirst_name());
        model.setLast_name(entity.getLast_name());
        model.setEmail(entity.getEmail());
        model.setUser_type(entity.getType());

        return model;
    }
    public static UserEntity toEntity(UserModel model){
        UserEntity entity =new UserEntity();

        entity.setId(model.getUserid());
        entity.setEmail(model.getEmail());
        entity.setType(model.getUser_type());
        entity.setFirst_name(model.getFirst_name());
        entity.setLast_name(model.getLast_name());


        return entity;
    }


}
