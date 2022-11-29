package com.ds.management.domain.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "person")
public class Person implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "pg-uuid")
    private UUID id;
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "first_name", nullable = true)
    private String firstName;
    @Column(name = "last_name", nullable = true)
    private String lastName;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    @OneToMany(mappedBy = "person")
    @ToString.Exclude
    private List<Device> devices;

}
