package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.EmployeeWrapper;
import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(UUID employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public boolean update(Employee employee) {
        return employeeRepository.update(employee);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public EmployeeWrapper getEmployeeWrapper() {
        List<Employee> employees = employeeRepository.findAll();
        return new EmployeeWrapper(employees);
    }

    @Override
    public boolean updateEmployees(List<Employee> employees) {
        boolean isUpdate = false;
        for (Employee employee : employees) {
            Employee existingEmployee = findById(employee.getEmployeeId());
            if (existingEmployee != null) {
                boolean updated = false;
                if(!existingEmployee.getEmployeeName().equals(employee.getEmployeeName())){
                    existingEmployee.setEmployeeName(employee.getEmployeeName());
                    updated = true;
                }
                if (!existingEmployee.getPhone().equals(employee.getPhone())){
                    existingEmployee.setPhone(employee.getPhone());
                    updated = true;
                }
                if (!existingEmployee.getEmailAddress().equals(employee.getEmailAddress())){
                    existingEmployee.setEmailAddress(employee.getEmailAddress());
                    updated = true;
                }
                if(updated) {
                    update(existingEmployee);
                    isUpdate = true;

                }

            }
        }
        return isUpdate;

    }

}
