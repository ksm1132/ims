package com.ims.inventorymgmtsys.entity;

import java.util.List;

public class EmployeeWrapper {
    private List<Employee> employees;

    public EmployeeWrapper() {
        // Default constructor
    }

    public EmployeeWrapper(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
