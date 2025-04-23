package org.example.delivery.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.delivery.domain.user.enums.UserRole;


@Entity
@Getter
@Setter
@Table(name="users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isDeleted = false;

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public void deactivate() {
        this.isDeleted = true;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }
}
