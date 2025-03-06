package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository {

    Employee findById(UUID id);

    List<Employee> findAll();

    boolean update(Employee employee);
    void save(Employee employee);

//    Employee findByName(String name);
}
