import React from 'react';
import Authenticator from '../api/Authenticator';

class LoginForm extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = { showInvalidCredentials: false };
        
        this.handleSubmit = this.handleSubmit.bind(this);  
    }
    
    handleSubmit(event)
    {
    	event.preventDefault();
    	
    	this.props.loaderOverlay.show();
    	
    	const authenticator = new Authenticator();
    	const credentials = {
    		username: document.getElementById('email').value,
    		password: document.getElementById('password').value
    	};
    	
    	authenticator.authenticate(
    		credentials, 
    		(tokenContainer) =>
    		{	
    			this.props.localStorage.storeUser(tokenContainer.user);
    			this.props.localStorage.storeToken(tokenContainer.token);
    			
    			document.getElementById('login-form').submit();
    		},
    		(error) => {
    			this.props.loaderOverlay.hide();
    			this.setState({ showInvalidCredentials: true });
    		}
		);
  	}
    
    render()
    {
    	const invalidClass = (this.state.showInvalidCredentials ? 'invalid' : '');
    
    	return (
	    	<form 
	    		id="login-form" 
	    		method="POST" 
	    		action="login" 
	    		onSubmit={this.handleSubmit}>
				<input 
					id="email" 
					class={ 'form-control ' + invalidClass } 
					type="text" 
					name="email"
					placeholder="E-Mail oder Benutzername" />
				<input 
					id="password" 
					class={ 'form-control ' + invalidClass } 
					type="password" 
					name="password"
					placeholder="Passwort" />
				<span class="float-left red-font">
					<p>{ this.state.showInvalidCredentials ? 'Ungültiger Benutzername oder Passwort' : '' }</p>
				</span>
				<span class="float-right">
					<p><a href="http://localhost:8080/ws18-swa10/public/register">Zur Registrierung</a></p>
				</span>
				<input class="btn btn-primary btn-block" type="submit" value="Login"/>
			</form>
        );
    }
}

export default LoginForm;