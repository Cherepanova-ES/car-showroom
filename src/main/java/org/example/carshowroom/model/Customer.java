package org.example.carshowroom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Customer {
    private Integer id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Car car;

    @Override
    public String toString() {
        return String.join(" ", this.lastName, this.firstName, this.patronymic);
    }
}
