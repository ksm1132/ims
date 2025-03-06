package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.controller.SessionController;
import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional
public class CartServiceImpl implements CartService{

    private final CatalogService catalogService;
    private final OrderService orderService;
    private final SessionController sessionController;

    public CartServiceImpl(CatalogService catalogService, OrderService orderService, SessionController sessionController) {
        this.catalogService = catalogService;
        this.orderService = orderService;
        this.sessionController = sessionController;
    }

    @Override
    public CartInput getCartInput() { return sessionController.getCartInput();};

    @Override
    public void setCartInput(CartInput cartInput) { sessionController.setCartInput(cartInput);}

    @Override
    public void addItemToCart(CartItemInput cartItemInput) {
        CartInput cartInput = getCartInput();
        if (cartInput == null) {
            cartInput = new CartInput();
            cartInput.setCartItemInputs(new ArrayList<>());
            setCartInput(cartInput);
        }

        boolean existItemInCart = false;
        for (CartItemInput item: cartInput.getCartItemInputs()) {
            if (item.getProductId().equals(cartItemInput.getProductId())) {
                item.setQuantity(item.getQuantity() + cartItemInput.getQuantity());
                existItemInCart = true;
                break;
            }
        }

        if (!existItemInCart) {
            Product product = catalogService.findById(cartItemInput.getProductId());
            cartItemInput.setId(UUID.randomUUID().toString());
            cartItemInput.setProductName(product.getName());
            cartItemInput.setProductPrice(product.getPrice());
            cartItemInput.setImgUrl(product.getImgUrl());
            cartInput.getCartItemInputs().add(cartItemInput);
        }

        calculatedAmounts(cartInput);

    }

    @Override
    public void removeItemFromCart(String cartItemId) {
        CartInput cartInput = getCartInput();
        CartItemInput target = null;
        for (CartItemInput item : cartInput.getCartItemInputs()) {
            if (cartItemId.equals(item.getId())) {
                target = item;
                break;
            }
        }
        if (target != null) {
            cartInput.getCartItemInputs().remove(target);
            calculatedAmounts(cartInput);
        }
    }

    @Override
    public void updateItemQuantity(String cartItemId, int quantity) {
        CartInput cartInput = getCartInput();
        if (cartInput != null) {
            for (CartItemInput item : cartInput.getCartItemInputs()) {
                if (item.getId().equals(cartItemId)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
            calculatedAmounts(cartInput);
        }

    }

    @Override
    public void calculatedAmounts(CartInput cartInput) {
        int totalAmount = orderService.calculateTotalAmount(cartInput.getCartItemInputs());
        int billingAmount = orderService.calculateTax(totalAmount);
        cartInput.setTotalAmount(totalAmount);
        cartInput.setBillingAmount(billingAmount);
    }

    @Override
    public Product getProduct(UUID productId) {
        return catalogService.findById(productId);
    }

}
