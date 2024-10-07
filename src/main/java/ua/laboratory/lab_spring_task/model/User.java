package ua.laboratory.lab_spring_task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(nullable = false)
    protected String firstName;
    @Column(nullable = false)
    protected String lastName;
    @Column(nullable = false, unique = true)
    protected String username;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false)
    protected boolean active;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, String username, String password, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
    }
}
