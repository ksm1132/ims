package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.entity.*;
import com.ims.inventorymgmtsys.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;

    private final ProductService productService;
    private final EmployeeService employeeService;

    private final SalesService salesService;

    public AdminController(OrderService orderService, ProductService productService, EmployeeService employeeService, SalesService salesService) {
        this.orderService = orderService;
        this.productService = productService;
        this.employeeService = employeeService;
        this.salesService = salesService;
    }

    @GetMapping("/management")
    public String displayList(Model model) {
        model.addAttribute("productWrapper", productService.getProductWrapper());
        model.addAttribute("orderList", orderService.findAll());
        model.addAttribute("newProduct", new Product());
        return "admin/adminManagement";
    }


    @PostMapping("/update-products")
    public String updateProducts(@ModelAttribute("productWrapper") ProductWrapper productWrapper, Model model) {
        List<Product> products = productWrapper.getProducts();
        boolean isUpdate;
        try {
            isUpdate = productService.updateProducts(products);

            if (isUpdate) {
                model.addAttribute("successMessage", "商品を更新しました。");
            } else {
                model.addAttribute("successMessage", "更新はありません");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "商品の更新中にエラーが発生しました。" + e.getMessage());
        }

            model.addAttribute("productWrapper", new ProductWrapper(productService.findAll()));
            model.addAttribute("newProduct", new Product());
            return "admin/adminManagement";
        }

    @PostMapping("add-product")
    public String addProduct(@ModelAttribute("newProduct") @Valid Product product, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("errorMessage2", "商品の追加中にエラーが発生しました");
            return "admin/adminManagement";
        }

        try {
            productService.save(product);
            model.addAttribute("successMessage2", "商品を追加しました");
        }catch (Exception e){
            model.addAttribute("errorMessage2", "商品の追加中にエラーが発生しました" + e.getMessage());
        }
        model.addAttribute("orderList", orderService.findAll());
        model.addAttribute( "productWrapper", new ProductWrapper(productService.findAll()));
        return "admin/adminManagement";
    }

    @GetMapping("/employees")
    public String displayEmployeeList(Model model) {
        model.addAttribute("employeeWrapper", employeeService.getEmployeeWrapper());
        model.addAttribute("employeeList", employeeService.findAll());
        model.addAttribute("newEmployee", new Employee());

        return "admin/adminEmployees";
    }


    @PostMapping("/update-employees")
    public String updateEmployee(@ModelAttribute("employeeWrapper") EmployeeWrapper employeeWrapper, Model model) {
        List<Employee> employees = employeeWrapper.getEmployees();
        boolean isUpdate;
        try {
            isUpdate = employeeService.updateEmployees(employees);
            if (isUpdate) {
                model.addAttribute("successMessage", "人事データを更新しました。");
            } else {
                model.addAttribute("successMessage", "更新はありません");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "更新中にエラーが発生しました。" + e.getMessage());
        }

        model.addAttribute("employeeWrapper", new EmployeeWrapper(employeeService.findAll()));
        model.addAttribute("newEmployee", new Employee());
        return "admin/adminEmployees";
    }

    @PostMapping("add-employee")
    public String addEmployee(@ModelAttribute("newEmployee") @Valid Employee employee, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("errorMessage2", "人事の追加中にエラーが発生しました");
            return "admin/adminEmployees";
        }

        try {
            employeeService.save(employee);
            model.addAttribute("successMessage2", "人事データを追加しました");
        }catch (Exception e){
            model.addAttribute("errorMessage2", "人事データの追加中にエラーが発生しました" + e.getMessage());
        }
        model.addAttribute("employeeWrapper", new EmployeeWrapper(employeeService.findAll()));
        return "admin/adminEmployees";
    }



    @GetMapping("/orders")
    public String displayOrderList(Model model) {
        model.addAttribute("orderList", orderService.findAll());
        return "admin/adminOrders";
    }




    @GetMapping("sales")
    public String getSales(Model model) {
        model.addAttribute("graphUrl", "/admin/sales/chart");
        model.addAttribute("dailyGraphUrl", "/admin/sales/daily-chart");
        return "admin/adminSales";
    }

    @GetMapping("/sales/chart")
    public void getSalesChart(HttpServletResponse httpServletResponse) throws IOException {
        salesService.generateSalesChart(httpServletResponse);
    }

    @GetMapping("/sales/daily-chart")
    public void getDailySalesChart(HttpServletResponse httpServletResponse) throws IOException {
        salesService.generateDailySalesChart(httpServletResponse);
    }

    @GetMapping("/salesnew")
    public String getSalesDataNew(Model model) {
        System.out.println("sales data json:::::::::::::" + salesService.getSalesDataAsJson() );
        System.out.println("Daily sales data json:::::::::::::" + salesService.getDailySalesDataAsJson() );
        model.addAttribute("salesData", salesService.getSalesDataAsJson());
        model.addAttribute("dailySalesData", salesService.getDailySalesDataAsJson());
        return "admin/adminSalesNew";
    }



}
