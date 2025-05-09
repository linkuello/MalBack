package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsMessage {
    private String email;
    private String topic;     // "ads", "receipts", "account", "other"
    private String content;
}
