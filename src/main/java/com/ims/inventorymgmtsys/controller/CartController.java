package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.service.CartService;
import net.snowflake.client.jdbc.internal.org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add-item")
    public String addToCart(@Validated CartItemInput cartItemInput, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", cartService.getProduct(cartItemInput.getProductId()));
            return "cart/cartItem";
        }
        cartService.addItemToCart(cartItemInput);
        CartInput cartInput = cartService.getCartInput();
        model.addAttribute("cartInput", cartInput);
        return "cart/cartItem";
    }

    @GetMapping("/display-cart")
    public String displayCart(Model model) {
        CartInput cartInput = cartService.getCartInput();
        if (cartInput == null) {
            cartInput = new CartInput();
        }
        model.addAttribute("cartInput", cartInput);
        return "cart/cartItem";
    }

    @PostMapping("remove-item")
    public String removeItem(String cartItemId, Model model) {
        cartService.removeItemFromCart(cartItemId);
        model.addAttribute("cartInput", cartService.getCartInput());
        return "cart/cartItem";
    }

    @PostMapping("update-quantity")
    public String updateQuantity(@RequestParam("cartItemId") String cartItemId, @RequestParam("quantity") int quantity, Model model) {
        cartService.updateItemQuantity(cartItemId, quantity);
        model.addAttribute("cartInput", cartService.getCartInput());
        return "cart/cartItem";
    }

}

