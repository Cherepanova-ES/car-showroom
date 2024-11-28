package org.example.carshowroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Car {
    private Integer id;
    private String type;
    private String brand;
    private String model;
    private Short year;
    private BigDecimal engineVolume;
    private Short enginePower;

    public Car(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.join(" ", this.brand, this.model);
    }
}
