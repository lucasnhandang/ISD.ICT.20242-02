package com.hustict.aims.service.email.builder;

import com.hustict.aims.dto.email.BaseEmailRequest;
import jakarta.servlet.http.HttpSession;

public interface EmailRequestBuilder {
    String getType(); 
    BaseEmailRequest build(HttpSession session);
}
