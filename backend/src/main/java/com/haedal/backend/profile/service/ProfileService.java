package com.haedal.backend.profile.service;


import com.haedal.backend.auth.dto.UserDto;
import com.haedal.backend.auth.dto.request.UserUpdateRequest;
import com.haedal.backend.auth.model.User;

import java.util.Optional;


public interface ProfileService extends CrudService<User,Long>{

    User findById(String id);

    User save(User user);
}