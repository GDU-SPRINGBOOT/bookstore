package com.example.bookstorebackend.Domain.Respones;

import lombok.Builder;


@Builder
public record AuthenticationResponse(String username
        , String name
        , String role
        , String token
        , String message) {
}
