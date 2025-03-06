package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.exception.UserAlreadyExistsException;
import com.ims.inventorymgmtsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() { return "auth/register"; }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute User user, BindingResult bindingResult, Model model)  {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.registerUser(user);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorMessage", "User already exists");
            return "auth/register";
        }

        return "auth/registerCompletion";
    }


}
