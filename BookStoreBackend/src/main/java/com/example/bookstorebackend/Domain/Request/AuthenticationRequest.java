package com.example.bookstorebackend.Domain.Request;

import lombok.Builder;

public record AuthenticationRequest(String email,
                                    String password) {


}