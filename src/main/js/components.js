import React from 'react';
import {Thrift, TUserServiceClient} from './thrift-client.js';
import {DefaultButton} from 'pui-react-buttons';
import {Divider} from 'pui-react-dividers';
import {DefaultH1, DefaultH2, DefaultH3, DefaultH4, DefaultH5, DefaultH6, AlternateH1, AlternateH2, AlternateH3, AlternateH4, AlternateH5, AlternateH6, MarketingH1, MarketingH2, MarketingH3, MarketingH4, MarketingH5, MarketingH6, Heading} from 'pui-react-typography'
import {SortableTable, TableHeader, TableRow, TableCell} from 'pui-react-sortable-table';
import {EventEmitter} from 'events';

const transport = new Thrift.Transport("http://localhost:8080/user");
const protocol = new Thrift.Protocol(transport);
const client = new TUserServiceClient(protocol);

class EventManager {
    constructor() {
        this.emitter = new EventEmitter();
        this.cache = {};
    }

    event(type) {
        if (this.cache[type]) {
            return this.cache[type];
        }
        let ev = {
            subscribe: (handler) => {
                this.emitter.addListener(type, handler);
            },
            publish: (payload) => {
                this.emitter.emit(type, payload);
            },
            unsubscribe: () => {
                this.emitter.removeListener(type);
                delete this.cache[type];
            }
        };
        this.cache[type] = ev;
        return ev;
    }

    loadUsers() {
        return this.event('LOAD_USERS');
    }
}

const manager = new EventManager();

export default class HelloWorld extends React.Component {
    render() {
        return (
            <div className="container">
                <DefaultH1>React.js Sample</DefaultH1>
                <DefaultButton onClick={this.handleClick}>Reload</DefaultButton>
                <Divider />
                <Display />
            </div>
        );
    }

    handleClick() {
        manager.loadUsers().publish();
    }
}

class Display extends React.Component {
    constructor(props) {
        super(props);
        this.state = {users: []};
    }

    componentDidMount() {
        manager.loadUsers().subscribe(() => {
            client.findUsers((body) => {
                this.setState({users: body});
            });
        });
        manager.loadUsers().publish();
    }

    componentWillUnmount() {
        manager.loadUsers().unsubscribe();
    }

    render() {
        const body = this.state.users.map((user, key) => {
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
                    <TableHeader sortable={true}>LastName</TableHeader>
                  ]}
                className="table-light">
                {body}
            </SortableTable>
        );
    }
}