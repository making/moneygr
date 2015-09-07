package moneygr.thrift;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import moneygr.domain.model.AuditDateTime;
import moneygr.domain.model.Family;
import moneygr.domain.model.Role;
import moneygr.domain.model.User;
import moneygr.domain.repository.user.FamilyRepository;
import moneygr.domain.repository.user.RoleRepository;
import moneygr.domain.repository.user.UserRepository;

import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ThriftUserService implements TUserService.Iface {
	@Autowired
	UserRepository userRepository;
	@Autowired
	FamilyRepository familyRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public List<TFamily> findFamilies() throws TException {
		return familyRepository.findAll().stream().map(this::familyToTFamily)
				.collect(Collectors.toList());
	}

	@Override
	public List<TUser> findUsers() throws TException {
		return userRepository.findAll().stream().map(this::userToTUser)
				.collect(Collectors.toList());
	}

	@Override
	public TUser findUser(int userId) throws TException {
		User user = userRepository.findOne(userId);
		return userToTUser(user);
	}

	@Override
	public TUser create(TUser tUser, String rawPassword) throws TException {
		String encoded = passwordEncoder.encode(rawPassword);
		User user = tUserToUser(tUser);
		user.setPassword(encoded);
		user.setAuditDateTime(AuditDateTime.now());
		user = userRepository.saveAndFlush(user);
		return userToTUser(user);
	}

	TUser withUpdate(TUser user, Consumer<User> consumer) throws TException {
		User target = userRepository.findOne(user.getUserId());
		User source = tUserToUser(user);
		BeanUtils.copyProperties(source, target, "password", "auditDateTime", "version");
		target.setAuditDateTime(AuditDateTime.now());
		consumer.accept(target);
		User updated = userRepository.saveAndFlush(target);
		return userToTUser(updated);
	}

	@Override
	public TUser updateWithoutPassword(TUser user) throws TException {
		return withUpdate(user, x -> {
		});
	}

	@Override
	public TUser updateWithPassword(TUser user, String rawPassword) throws TException {
		return withUpdate(user, x -> {
			String encoded = passwordEncoder.encode(rawPassword);
			x.setPassword(encoded);
		});
	}

	@Override
	public void deleteUser(int userId) throws TException {
		userRepository.delete(userId);
	}

	TUser userToTUser(User user) {
		return new TUser().setUserId(user.getUserId()).setEmail(user.getEmail())
				.setFirstName(user.getFirstName()).setLastName(user.getLastName())
				.setRoles(user.getRoles().stream()
						.map(x -> new TRole().setRoleName(x.getRoleName()))
						.collect(Collectors.toList()))
				.setFamily(familyToTFamily(user.getFamily()));
	}

	TFamily familyToTFamily(Family family) {
		return new TFamily().setFamilyId(family.getFamilyId())
				.setFamilyName(family.getFamilyName());
	}

	User tUserToUser(TUser tUser) {
		User user = new User();
		if (tUser != null) {
			user.setUserId(tUser.getUserId());
			user.setEmail(tUser.getEmail());
			user.setFirstName(tUser.getFirstName());
			user.setLastName(tUser.getLastName());
			user.setRoles(tUser.getRoles().stream().map(x -> {
				Role role = new Role();
				role.setRoleName(x.getRoleName());
				role.setVersion(0 /* dummy */);
				return role;
			}).collect(Collectors.toList()));
			user.setFamily(tFamilyToFamily(tUser.getFamily()));
		}
		return user;
	}

	Family tFamilyToFamily(TFamily tFamily) {
		Family family = new Family();
		if (tFamily != null) {
			family.setFamilyId(tFamily.getFamilyId());
			family.setFamilyName(tFamily.getFamilyName());
			family.setVersion(0 /* dummy */);
		}
		return family;
	}
}
