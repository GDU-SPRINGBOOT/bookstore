package com.example.bookstorebackend.Domain.Respones;

import lombok.Builder;

@Builder
public record ResponseObject(int status,
                             String message,
                             Object data) {
}
