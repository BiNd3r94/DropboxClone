import React from 'react';
import 'bootstrap';
import NavBar from './NavBar';
import Logo from './Logo';
import SidebarMenu from './SidebarMenu';

class AppPageContainer extends React.Component
{
	constructor(props)
	{
		super(props);
		
		this.state = { 
			showErrorPopup: false,
			error: null
		};
		
		this.showErrorPopup = this.showErrorPopup.bind(this);
		this.hideErrorPopup = this.hideErrorPopup.bind(this);
		this.errorPopup = { show: this.showErrorPopup };
    }
    
    showErrorPopup(error)
    {
    	const response = error.responseJSON;
    	let message;
    	
    	if (response != null && response.hasOwnProperty('message')) {
    		message = response.message;
    	} else {
    		message = error.responseText;
    	}
    
    	this.setState({ showErrorPopup: true, error: message });
    }
    
    hideErrorPopup(event)
    {
    	event.preventDefault();
    
    	this.setState({ showErrorPopup: false, error: null });
    }
    
    render()
    {
    	return (
    		<div id="section-container" class="row">
    			<div id="left-section" class="col-md-3">
    				<Logo width={ 32 } height={ 32 } />
    				<SidebarMenu />
    			</div>
    			<div id="right-section" class="col-md-9">
    				<div class="row">
		    			<div class="col-md-9">
		    				<div class="container">
		    					{ this.props.renderContent(this.errorPopup) }
				    		</div>
		    			</div>
		    			<div class="col-md-3">
		    				<NavBar />
							{ this.props.renderControls(this.errorPopup) }
	    				</div>
    				</div>
    			</div>
    			<div 
    				id="error-popup" 
    				class={ "alert alert-danger " + (this.state.showErrorPopup ? 'visible' : 'invisible') }>
		        	<button type="button" class="close" onClick={ (e) => this.hideErrorPopup(e) }>×</button>
		          	{ this.state.error == null ? '' : this.state.error }
	          	</div>
    		</div>
    	);
    }
}

export default AppPageContainer;