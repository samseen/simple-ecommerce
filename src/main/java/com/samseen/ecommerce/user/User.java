package com.samseen.ecommerce.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samseen.ecommerce.enums.Role;
import com.samseen.ecommerce.token.entity.Token;
import com.samseen.ecommerce.user.entity.Permission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private String updatedBy;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;
}
