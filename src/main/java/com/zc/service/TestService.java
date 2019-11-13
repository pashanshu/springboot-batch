package com.zc.service;

import java.util.List;

import com.zc.model.Access;

public interface TestService {
    public List<Access> queryAll();
    public Access queryByName(String name);
    public int insert(Access access);
}
