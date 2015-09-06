package moneygr.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;
import moneygr.domain.model.converter.LocalDateTimeConverter;

@Embeddable
@Data
public class AuditDateTime implements Serializable {
	@NotNull
	@Column(name = "created_at", updatable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime createdAt;
	@NotNull
	@Column(name = "updated_at")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime updatedAt;

	public static AuditDateTime now() {
		AuditDateTime auditDateTime = new AuditDateTime();
		LocalDateTime now = LocalDateTime.now();
		auditDateTime.setCreatedAt(now);
		auditDateTime.setUpdatedAt(now);
		return auditDateTime;
	}
}
