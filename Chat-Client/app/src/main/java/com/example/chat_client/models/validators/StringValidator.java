package com.example.chat_client.models.validators;

import java.io.Serializable;

public interface StringValidator extends Serializable {
    boolean isValid(String value);
}
