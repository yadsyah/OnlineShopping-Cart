package com.diansetiyadi.myapp.dao;

import com.diansetiyadi.myapp.entity.AccountsEntity;

public interface AccountDAO {

    public AccountsEntity findAccount(String name);
}
