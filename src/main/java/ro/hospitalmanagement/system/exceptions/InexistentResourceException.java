package ro.hospitalmanagement.system.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InexistentResourceException extends Exception {
    private String message;
    private String name;
    private String lastname;

    public InexistentResourceException(String message, String name) {
        this.message = message;
        this.name = name;
    }
    public InexistentResourceException(String message) {
        this.message = message;
    }
}