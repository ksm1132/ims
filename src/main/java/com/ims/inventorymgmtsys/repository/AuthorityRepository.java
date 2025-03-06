package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Authorities;

import java.util.List;

public interface AuthorityRepository {

    List<Authorities> getRole(String username);

    void saveAuthority(Authorities authority);  // 新しいメソッドの追加
}
