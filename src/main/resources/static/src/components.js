import React from 'react';
import {Thrift, TUserServiceClient} from './thrift-client.js';

var transport = new Thrift.Transport("http://localhost:8080/user");
var protocol = new Thrift.Protocol(transport);
var client = new TUserServiceClient(protocol);

export default class HelloWorld extends React.Component {
    render() {
        return (
            <div>
                <h1>React.js Sample</h1>
                <Display />
            </div>
        );
    }
}

class Display extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    componentDidMount() {
        client.findUser(1, (body) => {
            this.setState(body);
        });
    }

    render() {
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
}