package moneygr.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Entity
@Table(name = "family")
@ToString(exclude = "members")
@EqualsAndHashCode(exclude = "members")
@Data
public class Family implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "family_id")
	private Integer familyId;
	@Size(min = 1, max = 255)
	//@NotNull
	@Column(name = "family_name")
	private String familyName;
	@OneToMany(mappedBy = "family")
	private List<User> members;
	@Embedded
	//@Valid
	@JsonUnwrapped
	private AuditDateTime auditDateTime;
	@Version
	//@NotNull
	@Column(name = "version")
	private Integer version;
}
