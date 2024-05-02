package com.example.bookstorebackend.Domain.Respones;

import lombok.Builder;

@Builder
public record RegistrationResponseWrapper(int status,
                                          String message,
                                          boolean success,
                                          RegistrationResponse user) {
}
