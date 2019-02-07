import React from 'react';
import {render} from 'react-dom';
import Logo from './components/Logo';
import LocalStorage from './api/LocalStorage';
import LoginForm from './components/LoginForm';

class LoginApp extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = { showLoaderOverlay: false };
        
        this.showLoaderOverlay = this.showLoaderOverlay.bind(this);
        this.hideLoaderOverlay = this.hideLoaderOverlay.bind(this);
        
        this.loaderOverlay = {
        	show: this.showLoaderOverlay,
        	hide: this.hideLoaderOverlay
        };
    }
    
    showLoaderOverlay()
    {
    	this.setState({ showLoaderOverlay: true });
    }
    
    hideLoaderOverlay()
    {
    	this.setState({ showLoaderOverlay: false });
    }
    
    render()
    {
    	const localStorage = new LocalStorage();
    
    	return (
    		<div id="login-container">
    			<h3><Logo width={ 32 } height={ 32 } /> Login</h3>
    			<LoginForm loaderOverlay={ this.loaderOverlay } localStorage={ localStorage }/>
    			<section id="loader-overlay" class={ this.state.showLoaderOverlay ? 'active' : '' }>
					<img src="assets/img/loader.gif"></img>
				</section>
			</div>
    	);
    }
}

render(<LoginApp/>, document.getElementById('root'));