package ru.gothmog.blog.app.ws.io.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "auth")
public class AddressEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(name = "address_id", length=30, nullable=false)
    private String addressId;

    @Column(name = "city", length=15, nullable=false)
    private String city;

    @Column(name = "country", length=15, nullable=false)
    private String country;

    @Column(name = "street_name", length=100, nullable=false)
    private String streetName;

    @Column(name = "postal_code", length=7, nullable=false)
    private String postalCode;

    @Column(name="type_address", length=10, nullable=false)
    private String type;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_address_user"))
    @JsonIgnore
    private UserEntity userDetails;
}
