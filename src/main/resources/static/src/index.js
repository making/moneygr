var Thrift = require('thrift');
var TUserServiceClient = require('TUserServiceClient');

var transport = new Thrift.Transport("http://localhost:8080/user");
var protocol = new Thrift.Protocol(transport);
var client = new TUserServiceClient(protocol);

client.findUser(1, function (user) {
    document.getElementById('example').innerHTML = JSON.stringify(user);
});
