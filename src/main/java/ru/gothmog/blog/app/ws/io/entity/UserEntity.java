package ru.gothmog.blog.app.ws.io.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "auth",
        indexes = {@Index(name = "unq_user_email", columnList = "email", unique = true),
                   @Index(name = "unq_user_first_name", columnList = "first_name", unique = true),
                   @Index(name = "unq_user_id",columnList = "user_id",unique = true)})
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false, name = "user_id")
    private String userId;
    @Column(nullable = false, length = 50, name = "first_name")
    private String firstName;
    @Column(nullable = false, length = 250, name = "last_name")
    private String lastName;
    @Column(nullable = false, length = 150, name = "email")
    private String email;
    @Column(nullable = false, name = "encrypted_password")
    private String encryptedPassword;
    @Column(name = "email_verification_token")
    private String emailVerificationToken;
    @Column(nullable = false)
    private Boolean emailVerificationStatus;
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AddressEntity> addresses = new ArrayList<>();
}
