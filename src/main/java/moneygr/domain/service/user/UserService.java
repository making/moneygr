package moneygr.domain.service.user;

import java.util.List;
import java.util.function.Consumer;

import moneygr.domain.model.AuditDateTime;
import moneygr.domain.model.Family;
import moneygr.domain.model.User;
import moneygr.domain.repository.user.FamilyRepository;
import moneygr.domain.repository.user.RoleRepository;
import moneygr.domain.repository.user.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	FamilyRepository familyRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public List<Family> findFamilies() {
		return familyRepository.findAll();
	}

	public List<User> findUsers() {
		return userRepository.findAll();
	}

	public User findUser(Integer userId) {
		return userRepository.findOne(userId);
	}

	public User create(User user, String rawPassword) {
		String encoded = passwordEncoder.encode(rawPassword);
		user.setPassword(encoded);
		user.setAuditDateTime(AuditDateTime.now());
		user = userRepository.saveAndFlush(user);
		return user;
	}

	User withUpdate(User source, Consumer<User> consumer) {
		User target = userRepository.findOne(source.getUserId());
		BeanUtils.copyProperties(source, target, "password", "auditDateTime", "version");
		target.setAuditDateTime(AuditDateTime.now());
		consumer.accept(target);
		User updated = userRepository.saveAndFlush(target);
		return updated;
	}

	public User updateWithPassword(User user, String rawPassword) {
		return this.withUpdate(user, (x) -> {
			String encoded = passwordEncoder.encode(rawPassword);
			x.setPassword(encoded);
		});
	}

	public User updateWithoutPassword(User user) {
		return this.withUpdate(user, (x) -> {
		});
	}

	public void deleteUser(Integer userId) {
		userRepository.delete(userId);
	}
}
