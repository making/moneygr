package moneygr.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import lombok.ToString;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Entity
@Table(name = "\"user\"")
@Data
@ToString(exclude = "password")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	@Size(min = 1, max = 255)
	@NotNull
	@Email
	@Column(name = "email", unique = true)
	private String email;
	@Size(min = 1, max = 255)
	@NotNull
	@Column(name = "password")
	private String password;
	@Size(min = 1, max = 255)
	@NotNull
	@Column(name = "first_name")
	private String firstName;
	@Size(min = 1, max = 255)
	@NotNull
	@Column(name = "last_name")
	private String lastName;
	@ManyToOne
	@JoinColumn(name = "family_id")
	private Family family;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id") , inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "role_name") )
	private List<Role> roles;
	@Embedded
	@Valid
	@JsonUnwrapped
	private AuditDateTime auditDateTime;
	@Version
	@NotNull
	@Column(name = "version")
	private Integer version;
}
