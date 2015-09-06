export var Thrift = window.Thrift;
export var TUserServiceClient = window.TUserServiceClient;
export var TUser = window.TUser;
export var TFamily = window.TFamily;
export var TRole = window.TRole;

const transport = new Thrift.Transport("http://localhost:8080/user");
const protocol = new Thrift.Protocol(transport);
export var userService = new TUserServiceClient(protocol);