import React from 'react';
import {DefaultButton, DangerButton} from 'pui-react-buttons';
import {Divider} from 'pui-react-dividers';
import {DefaultH1, DefaultH2, DefaultH3, DefaultH4, DefaultH5, DefaultH6, AlternateH1, AlternateH2, AlternateH3, AlternateH4, AlternateH5, AlternateH6, MarketingH1, MarketingH2, MarketingH3, MarketingH4, MarketingH5, MarketingH6, Heading} from 'pui-react-typography'
import {Thrift, TUserServiceClient, TUser, TRole, TFamily} from '../ThriftClient.js';

const transport = new Thrift.Transport("http://localhost:8080/user");
const protocol = new Thrift.Protocol(transport);
const client = new TUserServiceClient(protocol);

export default class UserForm extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.events.createUser().subscribe((user, password) => {
            client.create(user, password, () => {
                this.props.events.loadUsers().publish();
            });
        });
    }

    componentWillUnmount() {
        this.props.events.createUser().unsubscribe();
    }

    handleSubmit(e) {
        e.preventDefault();
        const password = React.findDOMNode(this.refs.password).value;
        const user = new TUser({
            email: React.findDOMNode(this.refs.email).value,
            firstName: React.findDOMNode(this.refs.firstName).value,
            lastName: React.findDOMNode(this.refs.lastName).value,
            roles: [new TRole({roleName: 'USER'}), new TRole({roleName: 'ADMIN'})],
            family: new TFamily({familyId: 1})
        });
        this.props.events.createUser().publish(user, password);
        for (let k in this.refs) {
            React.findDOMNode(this.refs[k]).value = '';
        }
    }

    render() {
        return (
            <form role="form" onSubmit={this.handleSubmit.bind(this)}>
                <div className="form-group">
                    <label htmlFor="email">Email address</label>
                    <input type="email" className="form-control" id="email" ref="email" placeholder="Enter email"/>
                </div>
                <div className="form-group">
                    <label htmlFor="firstName">First name</label>
                    <input type="text" className="form-control" id="firstName" ref="firstName"
                           placeholder="Enter first name"/>
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last name</label>
                    <input type="text" className="form-control" id="lastName" ref="lastName"
                           placeholder="Enter last name"/>
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input type="password" className="form-control" id="password" ref="password"
                           placeholder="Enter password"/>
                </div>
                <div className="checkbox">
                    <label>
                        <input ref="admin" type="checkbox">Admin</input>
                    </label>
                </div>
                <DefaultButton>Create</DefaultButton>
            </form>
        );
    }
}