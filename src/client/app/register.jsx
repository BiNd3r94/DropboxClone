import React from 'react';
import {render} from 'react-dom';
import Logo from './components/Logo';
import RegisterForm from './components/RegisterForm';
import UserClient from './api/public/UserClient';

class RegisterApp extends React.Component
{
	constructor(props)
	{
        super(props);

        this.state = { showWasRegisteredView: false };

        this.switchToShowWasRegisteredView = this.switchToShowWasRegisteredView.bind(this);
    }

    switchToShowWasRegisteredView()
    {
        this.setState({ showWasRegisteredView: true });
    }
    
    render()
    {
        const userClient = new UserClient();

        if (this.state.showWasRegisteredView) {
            return (
                <div id="register-container">
                    <p>Sie wurden erfolgreich registriert</p>
                    <a class="btn btn-primary btn-block" href="http://localhost:8080/ws18-swa10/public/login">Zum Login</a>
                </div>
            );
        } else {
            return (
                <div id="register-container">
                    <h3><Logo width={ 32 } height={ 32 }/> Registrierung</h3>
                    <RegisterForm userClient={ userClient } callback={ this.switchToShowWasRegisteredView } />
                </div>
            );
        }
    }
}

render(<RegisterApp/>, document.getElementById('root'));