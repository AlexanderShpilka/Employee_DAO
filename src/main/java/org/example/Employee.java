package org.example;

import java.math.BigDecimal;

public record Employee(Integer id, String name, int age, String position, BigDecimal salary) {
    public Employee {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("age must be between 0 and 120, but was " + age);
        }

        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }

        if (salary == null) {
            throw new IllegalArgumentException("salary cannot be null");
        }

        if (salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("salary cannot be negative");
        }
    }
}
