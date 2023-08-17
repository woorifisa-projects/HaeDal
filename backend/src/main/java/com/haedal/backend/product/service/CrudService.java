package com.haedal.backend.product.service;

public interface CrudService <T,ID> {
    T findById(ID userId);

}
