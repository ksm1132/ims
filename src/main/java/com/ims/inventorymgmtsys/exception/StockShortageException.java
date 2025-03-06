package com.ims.inventorymgmtsys.exception;

public class StockShortageException extends RuntimeException{
    public StockShortageException(String msg) { super(msg); }
}
