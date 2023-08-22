package com.haedal.backend.profile.service;


import com.haedal.backend.auth.model.User;


public interface ProfileService extends CrudService<User,Long>{

    User findById(String id);

}