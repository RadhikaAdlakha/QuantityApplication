package com.quantity.measurement.dto;

public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String email;
    private String name;
    private String picture;
    private long   expiresIn;

    public AuthResponseDTO(String token, String email, String name,
                           String picture, long expiresIn) {
        this.token     = token;
        this.email     = email;
        this.name      = name;
        this.picture   = picture;
        this.expiresIn = expiresIn;
    }

    public String getToken()     { return token; }
    public String getTokenType() { return tokenType; }
    public String getEmail()     { return email; }
    public String getName()      { return name; }
    public String getPicture()   { return picture; }
    public long   getExpiresIn() { return expiresIn; }
}