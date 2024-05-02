package com.example.bookstorebackend.Domain.Request;

public record RegistrationRequest(String firstName,
                                  String lastName,
                                  String email,
                                  String password) {

}
