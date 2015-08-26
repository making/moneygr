var React = require('react');
var Thrift = require('thrift');
var TUserServiceClient = require('TUserServiceClient');

var transport = new Thrift.Transport("http://localhost:8080/user");
var protocol = new Thrift.Protocol(transport);
var client = new TUserServiceClient(protocol);

var HelloWorld = React.createClass({
    render: function () {
        return (
            <div>
                <h1>React.js Sample</h1>
                <Display />
            </div>
        );
    }
});

var Display = React.createClass({
    getInitialState: function () {
        return {};
    },
    componentDidMount: function () {
        client.findUser(1, function (body) {
            this.setState(body);
        }.bind(this));
    },
    render: function () {
        return (
            <table>
                <tr>
                    <th>UserId</th>
                    <td>{this.state.userId}</td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td>{this.state.email}</td>
                </tr>
                <tr>
                    <th>FirstName</th>
                    <td>{this.state.firstName}</td>
                </tr>
                <tr>
                    <th>LastName</th>
                    <td>{this.state.lastName}</td>
                </tr>
            </table>);
    }
});

module.exports = HelloWorld;