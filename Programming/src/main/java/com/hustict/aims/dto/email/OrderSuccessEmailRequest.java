package com.hustict.aims.dto.email;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderSuccessEmailRequest extends BaseEmailRequest {
    private String cancelLink;
}
