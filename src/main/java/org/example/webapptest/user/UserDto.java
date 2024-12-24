package org.example.webapptest.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    // Προσθέτεις άλλα πεδία όπως χρειάζεστε
}