package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.EmployeeWrapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(UUID id);
    boolean update(Employee employee);
    void save(Employee employee);
    EmployeeWrapper getEmployeeWrapper();
    boolean updateEmployees(List<Employee> employees);
}
