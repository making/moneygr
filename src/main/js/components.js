import React from 'react';
import {Thrift, TUserServiceClient} from './thrift-client.js';
import {DefaultButton} from 'pui-react-buttons';
import {Divider} from 'pui-react-dividers';
import {DefaultH1, DefaultH2, DefaultH3, DefaultH4, DefaultH5, DefaultH6, AlternateH1, AlternateH2, AlternateH3, AlternateH4, AlternateH5, AlternateH6, MarketingH1, MarketingH2, MarketingH3, MarketingH4, MarketingH5, MarketingH6, Heading} from 'pui-react-typography'
import {SortableTable, TableHeader, TableRow, TableCell} from 'pui-react-sortable-table';

var transport = new Thrift.Transport("http://localhost:8080/user");
var protocol = new Thrift.Protocol(transport);
var client = new TUserServiceClient(protocol);

export default class HelloWorld extends React.Component {
    render() {
        return (
            <div className="container">
                <DefaultH1>React.js Sample</DefaultH1>
                <DefaultButton>Reload</DefaultButton>
                <Divider />
                <Display />
            </div>
        );
    }
}
class Display extends React.Component {
    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    componentDidMount() {
        client.findUsers((body) => {
            this.setState({users: body});
        });
    }

    render() {
        var body = this.state.users.map(function (user, key) {
            return (
                <TableRow key={key}>
                    <TableCell>{user.userId}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.firstName}</TableCell>
                    <TableCell>{user.lastName}</TableCell>
                </TableRow>)
        });
        return (
            <SortableTable
                headers={[
                    <TableHeader sortable={true}>UserId</TableHeader>,
                    <TableHeader sortable={true}>Email</TableHeader>,
                    <TableHeader sortable={true}>FirstName</TableHeader>,
                    <TableHeader sortable={true}>LastNAme</TableHeader>
                  ]}
                className="table-light">
                {body}
            </SortableTable>
        );
    }
}