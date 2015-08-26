package moneygr.thrift;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

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

		{
			TUser user = client.findUser(1);
			System.out.println(user);
			assertThat(user, is(notNullValue()));
			assertThat(user.getUserId(), is(1));
		}
		{
			TUser user = client.findUser(2);
			System.out.println(user);
			assertThat(user, is(notNullValue()));
			assertThat(user.getUserId(), is(2));
		}

		System.out.println(client.findFamilies());

	}

	@Test
	public void testCreate() throws Exception {
		TUser user = client.create(
				new TUser().setEmail("maki@ik.am").setFirstName("俊明").setLastName("槙")
						.setFamily(new TFamily().setFamilyId(1))
						.setRoles(Arrays.asList(new TRole("USER"), new TRole("ADMIN"))),
				"password");
		System.out.println("Created " + user);
		assertThat(user, is(notNullValue()));
		assertThat(user.getUserId(), is(notNullValue()));
	}

}