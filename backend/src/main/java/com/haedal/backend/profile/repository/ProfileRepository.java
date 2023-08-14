package com.haedal.backend.profile.repository;

import com.haedal.backend.profile.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<User, Long> {

}
