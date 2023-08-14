package com.haedal.backend.profile.service;

public interface CrudService <T,ID> {
    T findById(ID id);
}