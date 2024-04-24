package org.example.accounts.utils;

public class Constants {
    public static final String INVALID_BIRTHDAY_MESSAGE = "Invalid customer birthday";
    public static final String INVALID_CURRENCY_PARAM_MESSAGE = "Invalid currency";
    public static final String NULL_BODY_EXCEPTION = "Null body retrieved from Converter service";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String CUSTOMER_ACCOUNT_NOT_FOUND = "Customer account not found";
    public static final String NOT_ENOUGH_MONEY = "Not enough money";
    public static final String AMOUNT_NOT_VALID = "Amount not valid";
    public static final String RATE_LIMIT_MESSAGE = "Rate limit exceeded";
    public static final String SERVICE_UNAVAILABLE = "Converter is unavailable";
    public static final String CUSTOMER_UPDATE_MESSAGE_TEMPLATE = "Счет %s. Операция: %s. Баланс: %s";
    public static final String DEFAULT_FEE_ENTITY_ID = "TRANSFERS";
    public static final String FEE_COLUMN_DEFINITION = "DECIMAL(9,8) CHECK (value >= 0.0 AND value <= 1.0)";
}
