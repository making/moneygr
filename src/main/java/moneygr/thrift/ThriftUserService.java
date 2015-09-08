package moneygr.thrift;

import java.util.List;
import java.util.stream.Collectors;

import moneygr.domain.model.Family;
import moneygr.domain.model.Role;
import moneygr.domain.model.User;
import moneygr.domain.service.user.UserService;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThriftUserService implements TUserService.Iface {
	@Autowired
	UserService userService;

	@Override
	public List<TFamily> findFamilies() throws TException {
		return userService.findFamilies().stream().map(this::familyToTFamily)
				.collect(Collectors.toList());
	}

	@Override
	public List<TUser> findUsers() throws TException {
		return userService.findUsers().stream().map(this::userToTUser)
				.collect(Collectors.toList());
	}

	@Override
	public TUser findUser(int userId) throws TException {
		User user = userService.findUser(userId);
		return userToTUser(user);
	}

	@Override
	public TUser create(TUser tUser, String rawPassword) throws TException {
		User user = tUserToUser(tUser);
		return userToTUser(userService.create(user, rawPassword));
	}

	@Override
	public TUser updateWithoutPassword(TUser user) throws TException {
		User target = tUserToUser(user);
		return userToTUser(userService.updateWithoutPassword(target));
	}

	@Override
	public TUser updateWithPassword(TUser user, String rawPassword) throws TException {
		User target = tUserToUser(user);
		return userToTUser(userService.updateWithPassword(target, rawPassword));
	}

	@Override
	public void deleteUser(int userId) throws TException {
		userService.deleteUser(userId);
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
