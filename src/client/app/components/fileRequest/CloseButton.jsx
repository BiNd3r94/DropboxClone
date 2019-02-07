import React from 'react';
import FileRequestClient from '../../api/users/FileRequestClient';

class CloseButton extends React.Component
{
	constructor(props)
	{
        super(props);
    }
    
    closeFileRequest(event)
    {
    	event.preventDefault();
    	    	
    	this.props.fileRequestClient.closeFileRequest(this.props.id, () => {
    		this.props.moveFromOpenToClosedFileRequests(this.props.id);
    	});
    }
	
	render()
	{
		return (
			<button class="dropdown-item" 
		    	type="button" 
		    	onClick={ (e) => this.closeFileRequest(e) }>
		    	Schließen
	    	</button>
		);
	}    
}

export default CloseButton;