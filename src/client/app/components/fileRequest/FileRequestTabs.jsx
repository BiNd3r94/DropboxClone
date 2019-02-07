import React from 'react';
import FileRequestList from './FileRequestList';
import CloseButton from './CloseButton';
import ReOpenButton from './ReOpenButton';

class FileRequestTabs extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = {
        	openFileRequests: null,
        	closedFileRequests: null
        };
        
        this.moveFromOpenToClosedFileRequests = this.moveFromOpenToClosedFileRequests.bind(this);
        this.moveFromClosedToOpenFileRequests = this.moveFromClosedToOpenFileRequests.bind(this);
        this.renderOpenActionButtons = this.renderOpenActionButtons.bind(this);
        this.renderClosedActionButtons = this.renderClosedActionButtons.bind(this);
        
        this.props.fileRequestClient.getAllOpen((fileRequests) => {
    		this.setState({ openFileRequests: fileRequests });
    	});
    	this.props.fileRequestClient.getAllClosed((fileRequests) => {
    		this.setState({ closedFileRequests: fileRequests });
    	});
    }
    
    moveFromOpenToClosedFileRequests(id)
    {
    	let cachedFileRequest;
    	const openFileRequests = this.state.openFileRequests.filter((f) => {
    		const match = f.id != id;
    		
    		if (! match) cachedFileRequest = f;
    		
    		return match;
    	});
    	
    	this.state.closedFileRequests.push(cachedFileRequest);
    	
    	this.setState({ 
    		openFileRequests: openFileRequests,
    		closedFileRequests: this.state.closedFileRequests
		});
    }
    
    moveFromClosedToOpenFileRequests(id)
    {
    	let cachedFileRequest;
    	const closedFileRequests = this.state.closedFileRequests.filter((f) => {
    		const match = f.id != id;
    		
    		if (! match) cachedFileRequest = f;
    		
    		return match;
    	});
    	
    	this.state.openFileRequests.push(cachedFileRequest);
    	
    	this.setState({ 
    		closedFileRequests: closedFileRequests,
    		openFileRequests: this.state.openFileRequests
		});
    }
    
    renderOpenActionButtons(id)
    {
    	return (
    		<div>
    			<CloseButton 
    				id={ id }
    				fileRequestClient={ this.props.fileRequestClient }
    				moveFromOpenToClosedFileRequests={ this.moveFromOpenToClosedFileRequests } />
    		</div>
    	);
    }
    
    renderClosedActionButtons(id)
    {
    	return (
    		<div>
    			<ReOpenButton  
    				id={ id }
    				fileRequestClient={ this.props.fileRequestClient }
    				moveFromClosedToOpenFileRequests={ this.moveFromClosedToOpenFileRequests } />
    		</div>
    	);
    }

    render()
    {
    	return (
    		<div id="file-request-tabs">
		    	<ul class="nav nav-tabs" role="tablist">
				  <li class="nav-item">
				    <a class="nav-link active" 
				    	id="home-tab" 
				    	data-toggle="tab" 
				    	href="#home" 
				    	role="tab" 
				    	aria-controls="home" 
				    	aria-selected="true">
				    	Offene Anfragen
				    </a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" 
				    	id="profile-tab" 
				    	data-toggle="tab" 
				    	href="#profile" 
				    	role="tab" 
				    	aria-controls="profile" 
				    	aria-selected="false">
				    	Geschlossene Anfragen
				    </a>
				  </li>
				</ul>
				<div class="tab-content" id="myTabContent">
				  <div class="tab-pane fade show active" 
				  	id="home" 
				  	role="tabpanel" 
				  	aria-labelledby="home-tab">
				  	<FileRequestList 
				  		fileRequests={ this.state.openFileRequests }
				  		renderActionButtons={ this.renderOpenActionButtons } />
				  </div>
				  <div class="tab-pane fade" 
				  	id="profile" 
				  	role="tabpanel" 
				  	aria-labelledby="profile-tab">
				  	<FileRequestList 
				  		fileRequests={ this.state.closedFileRequests }
				  		renderActionButtons={ this.renderClosedActionButtons }  />
			  	  </div>
				</div>
			</div>
        );
    }
}

export default FileRequestTabs;