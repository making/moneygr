import React from 'react';
import {DefaultButton, DangerButton} from 'pui-react-buttons';
import {Divider} from 'pui-react-dividers';
import {DefaultH1, DefaultH2, DefaultH3, DefaultH4, DefaultH5, DefaultH6, AlternateH1, AlternateH2, AlternateH3, AlternateH4, AlternateH5, AlternateH6, MarketingH1, MarketingH2, MarketingH3, MarketingH4, MarketingH5, MarketingH6, Heading} from 'pui-react-typography'
import UserTable from './UserTable.jsx';

export default class App extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="container">
                <DefaultH1>React.js Sample</DefaultH1>
                <DefaultButton onClick={this.handleClick.bind(this)}>Reload</DefaultButton>
                <Divider />
                <UserTable events={this.props.events}/>
            </div>
        );
    }

    handleClick() {
        this.props.events.loadUsers().publish();
    }
}