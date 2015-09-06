import React from 'react';
import {Thrift, TUserServiceClient} from '../ThriftClient.js';
import {DefaultButton, DangerButton} from 'pui-react-buttons';
import {DefaultH1, DefaultH2, DefaultH3, DefaultH4, DefaultH5, DefaultH6, AlternateH1, AlternateH2, AlternateH3, AlternateH4, AlternateH5, AlternateH6, MarketingH1, MarketingH2, MarketingH3, MarketingH4, MarketingH5, MarketingH6, Heading} from 'pui-react-typography'
import {SortableTable, TableHeader, TableRow, TableCell} from 'pui-react-sortable-table';

const transport = new Thrift.Transport("http://localhost:8080/user");
const protocol = new Thrift.Protocol(transport);
const client = new TUserServiceClient(protocol);

class DeleteButton extends React.Component {
    constructor(props) {
        super(props);
    }

    handleClick() {
        this.props.events.deleteUser().publish(this.props.user);
    }

    render() {
        return <DangerButton onClick={this.handleClick.bind(this)}>Delete</DangerButton>;
    }
}

export default class UserTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    componentDidMount() {
        this.props.events.loadUsers().subscribe(() => {
            client.findUsers((body) => {
                this.setState({users: body});
            });
        });
        this.props.events.deleteUser().subscribe((user) => {
            client.deleteUser(user.userId, () => {
                this.props.events.loadUsers().publish();
            });
        });
        this.props.events.loadUsers().publish();
    }

    componentWillUnmount() {
        this.props.events.loadUsers().unsubscribe();
        this.props.events.deleteUser().unsubscribe();
    }

    render() {
        const body = this.state.users.map((user, key) => {
            return (
                <TableRow key={key}>
                    <TableCell>{user.userId}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.firstName}</TableCell>
                    <TableCell>{user.lastName}</TableCell>
                    <TableCell>
                        <DeleteButton user={user} events={this.props.events}/>
                    </TableCell>
                </TableRow>)
        });
        return (
            <SortableTable
                headers={[
                    <TableHeader sortable={true}>UserId</TableHeader>,
                    <TableHeader sortable={true}>Email</TableHeader>,
                    <TableHeader sortable={true}>FirstName</TableHeader>,
                    <TableHeader sortable={true}>LastName</TableHeader>,
                    <TableHeader>Actions</TableHeader>
                  ]}
                className="table-light">
                {body}
            </SortableTable>
        );
    }
}