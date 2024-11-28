package org.example.carshowroom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConnectionProperties {
    private final String driver;
    private final String url;
    private final String user;
    private final String password;
}
