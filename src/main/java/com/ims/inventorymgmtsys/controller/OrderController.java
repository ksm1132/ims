package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.Order;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.OrderInput;
import com.ims.inventorymgmtsys.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orderform")
    public String orderForm(Model model) {
        model.addAttribute("employees", orderService.findAllEmployees());
        model.addAttribute("orderInput", new OrderInput());
        return "order/orderForm";
    }

    @PostMapping("/orderconfirm")
    public String orderConfirm(@Validated @ModelAttribute("orderInput") OrderInput orderInput, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order/orderForm";
        }

        Employee employee = orderService.findEmployeeById(orderInput.getEmployeeId());

        if (employee == null) {
            bindingResult.rejectValue("employeeId", "error.employeeId", "Invalid employee selected");
            model.addAttribute("employees", orderService.findAllEmployees());
            return "order/orderForm";
        }
        orderInput.setEmployeeName(employee.getEmployeeName());
        orderInput.setEmployeeId(employee.getEmployeeId());
        orderService.setOrderInput(orderInput);

        model.addAttribute("cartInput", orderService.getCartInput());
        model.addAttribute("orderInput", orderService.getOrderInput());
        model.addAttribute("employee", employee.getEmployeeName());
        return "order/orderConfirmation";
    }

    @PostMapping(value = "placeorder", params = "correct")
    public String correctOrder(@Validated @ModelAttribute("orderInput") OrderInput orderInput, Model model) {
        model.addAttribute("employees", orderService.findAllEmployees());
        model.addAttribute("orderInput", orderService.getOrderInput());
        return "order/orderForm";
    }

    @PostMapping(value = "placeorder", params = "correctproduct")
    public String correctProduct(@Validated @ModelAttribute("cartInput") CartInput cartInput, Model model) {
        model.addAttribute("cartInput", orderService.getCartInput());
        return "cart/cartItem";
    }

    @PostMapping(value = "placeorder", params = "placeorderconfirm")
    public String placeOrder(RedirectAttributes redirectAttributes) {
        try {
            Employee employee = orderService.findEmployeeById(orderService.getOrderInput().getEmployeeId());
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found");
            }

            Order order = orderService.placeOrder(orderService.getOrderInput(), orderService.getCartInput(), employee);
            redirectAttributes.addFlashAttribute("order", order);

            orderService.clearSessionData();

            return "redirect:/order/orderCompletion";
        } catch (Exception e) {
            e.printStackTrace();
            return "order/orderForm";
        }
    }

    @GetMapping("/orderCompletion")
    public String orderCompletion(Model model) {
        return "order/orderCompletion";
    }

    @GetMapping("orderlist")
    public String getOrderListForCurrentUser(Model model) {
        model.addAttribute("orderlist", orderService.getOrderListForCurrentUser());
        return "order/orderList";
    }

}
