package moneygr.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Embeddable
@Data
public class AuditDateTime implements Serializable {
	@NotNull
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	@NotNull
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public static AuditDateTime now() {
		AuditDateTime auditDateTime = new AuditDateTime();
		LocalDateTime now = LocalDateTime.now();
		auditDateTime.setCreatedAt(now);
		auditDateTime.setUpdatedAt(now);
		return auditDateTime;
	}
}
