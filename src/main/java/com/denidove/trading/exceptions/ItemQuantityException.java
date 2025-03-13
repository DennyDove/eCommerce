package com.denidove.trading.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemQuantityException extends RuntimeException {

    public ItemQuantityException() {
        super("Количество выбранного товара превышает остаток на складе.");
    }
}
