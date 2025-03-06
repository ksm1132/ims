package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Claim;

import java.util.List;

public interface CliaimRepository {
    List<Claim> selectAll();
}
