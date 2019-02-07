import React from 'react';
import FileRequestClient from '../../api/users/FileRequestClient';

class ReOpenButton extends React.Component
{
	constructor(props)
	{
        super(props);
    }
    
    openFileRequest(event)
    {
    	event.preventDefault();
    	    	
    	this.props.fileRequestClient.openFileRequest(this.props.id, () => {
    		this.props.moveFromClosedToOpenFileRequests(this.props.id);
    	});
    }
	
	render()
	{
		return (
			<button class="dropdown-item" 
		    	type="button" 
		    	onClick={ (e) => this.openFileRequest(e) }>
		    	Erneut öffnen
	    	</button>
		);
	}    
}

export default ReOpenButton;