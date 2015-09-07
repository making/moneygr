package moneygr.thrift;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Random;

import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.junit.Before;
import org.junit.Test;

public class TUserServiceTest {
	TUserService.Client client;

	@Before
	public void setup() throws Exception {
		TProtocolFactory protocolFactory = new TJSONProtocol.Factory();
		TTransport transport = new THttpClient("http://localhost:8080/user");
		TProtocol protocol = protocolFactory.getProtocol(transport);
		client = new TUserService.Client(protocol);
	}

	@Test
	public void test() throws Exception {

		System.out.println(client.findUsers());
		System.out.println(client.findFamilies());

	}

	@Test
	public void testCreate() throws Exception {
		TUser user = client.create(
				new TUser()
						.setEmail("created" + new Random().nextInt(1000) + "@example.com")
						.setFirstName("俊明").setLastName("槙")
						.setFamily(new TFamily().setFamilyId(1))
						.setRoles(Arrays.asList(new TRole("USER"), new TRole("ADMIN"))),
				"password");
		System.out.println("Created " + user);
		assertThat(user, is(notNullValue()));
		assertThat(user.getUserId(), is(notNullValue()));
	}

	@Test
	public void testUpdate() throws Exception {
		TUser user = client.findUsers().get(0);
		user.setEmail("updated" + new Random().nextInt(1000) + "@example.com");
		user.setRoles(Arrays.asList(new TRole("USER")));
		TUser updated = client.updateWithoutPassword(user);
		System.out.println("Updated " + updated);
		assertThat(updated, is(notNullValue()));
		assertThat(updated.getUserId(), is(user.getUserId()));
		assertThat(updated.getEmail(), is(user.getEmail()));
		assertThat(updated.getFirstName(), is(user.getFirstName()));
		assertThat(updated.getLastName(), is(user.getLastName()));
		assertThat(updated.getFamily(), is(user.getFamily()));
		assertThat(updated.getRoles(), is(user.getRoles()));
	}

	@Test
	public void testUpdateWithUpdate() throws Exception {
		TUser user = client.findUsers().get(0);
		user.setEmail("updated" + new Random().nextInt(1000) + "@example.com");
		user.setRoles(Arrays.asList(new TRole("USER")));
		TUser updated = client.updateWithPassword(user, "moneygr");
		System.out.println("Updated " + updated);
		assertThat(updated, is(notNullValue()));
		assertThat(updated.getUserId(), is(user.getUserId()));
		assertThat(updated.getEmail(), is(user.getEmail()));
		assertThat(updated.getFirstName(), is(user.getFirstName()));
		assertThat(updated.getLastName(), is(user.getLastName()));
		assertThat(updated.getFamily(), is(user.getFamily()));
		assertThat(updated.getRoles(), is(user.getRoles()));
	}
}