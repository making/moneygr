package moneygr.domain.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Entity
@Table(name = "role")
@Data
public class Role implements Serializable {
	@Id
	@Column(name = "role_name")
	private String roleName;
	@Embedded
	@Valid
	@JsonUnwrapped
	private AuditDateTime auditDateTime;
	@Version
	@NotNull
	@Column(name = "version")
	private Integer version;
}
