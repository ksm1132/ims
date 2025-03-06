package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcEmployeeRepository implements EmployeeRepository{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JdbcEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Employee findById(UUID employeeid) {
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
    @Transactional
    @Override
    public boolean update(Employee employee) {
        jdbcTemplate.queryForObject(
                "SELECT * FROM employee WHERE id = ? FOR UPDATE",
                new Object[]{employee.getEmployeeId()},
                (rs, rowNum) -> employee.getEmployeeId()  // 必要な情報を返すために、RowMapperを簡単に指定
        );

        int count = jdbcTemplate.update("UPDATE employee SET employeename=?, phone=?, emailaddress=? WHERE employeeid=?",
                employee.getEmployeeName(),
                employee.getPhone(),
                employee.getEmailAddress(),
                employee.getEmployeeId());

        return count != 0;
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
