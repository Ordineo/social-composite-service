package be.ordina.ordineo.resource;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class EmployeeResource {


    private Long id;

    private String username;
    private String firstName;

    private String lastName;
    private String linkedin;

    private String email;

    private String phoneNumber;

    private String function;

    private Unit unit;

    private String description;

    private String profilePicture;

    private Gender gender;

    private LocalDate birthDate;

    private LocalDate hireDate;

    private LocalDate startDate;

    private LocalDate resignationDate;
}