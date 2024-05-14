package com.courseshare.UserService.service;

import java.sql.Date;
import java.util.List;

import Model.UserModel;
import com.courseshare.UserService.Utils.UserUtils;
import com.courseshare.UserService.entity.UserEntity;
import com.courseshare.UserService.reposetory.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSreviceImpl implements UserSrevice{
    @Autowired
    private UserRepository repository;
    @Override
    public List<UserModel> getAll() {
        return repository.findAll().stream().map(UserUtils::toModel).toList();
    }

    @Override
    public UserModel getById(long id) {
        return UserUtils.toModel(repository.findById(id).get());
    }

    @Override
    public UserModel getByEmail(String email) {
        return UserUtils.toModel(repository.findByEmail(email).orElseThrow());
    }


    @RabbitListener(queues = "q.user-register")
    public void addUser(UserModel user) {
        UserEntity userEntity =UserUtils.toEntity(user);
        //Set creation time
        userEntity.setCreationDate(new Date(System.currentTimeMillis()));
        user = UserUtils.toModel(repository.save(userEntity));
        log.info("user created :{}",user.getUserid() );
    }
}
