package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcEmployeeRepository implements EmployeeRepository{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JdbcEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Employee findById(String employeeid) {
        return jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employeeid = ?", new DataClassRowMapper<>(Employee.class), employeeid);
    }

//    @Override
//    public Employee findByName(String name) {
//        return jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employeename = ?", new DataClassRowMapper<>(Employee.class), name);
//    }


    @Override
    public List<Employee> findAll() {
        return jdbcTemplate.query("SELECT * FROM employee", new DataClassRowMapper<>(Employee.class));
    }

    @Override
    public boolean update(Employee employee) {
        int count = jdbcTemplate.update("UPDATE employee SET employeename=?, phone=?, emailaddress=? WHERE employeeid=?",
                employee.getEmployeeName(),
                employee.getPhone(),
                employee.getEmailAddress(),
                employee.getEmployeeId());

        if ( count == 0) {
            return false;
        }

        return true;
    }

    @Override
    public void save(Employee employee) {
        jdbcTemplate.update("INSERT INTO employee (employeename, phone, emailaddress) VALUES (?, ?, ?)",
                employee.getEmployeeName(),
                employee.getPhone(),
                employee.getEmailAddress()
                );
    }
}
