import React from 'react';
import FolderListTable from '../file/FolderListTable';
import BootstrapModal from '../BootstrapModal';

class CreateFileRequestModal extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = { 
        	showResultToken: false, 
        	resultToken: null 
    	};
        
        this.currentPath = '';  
        this.setCurrentPath = this.setCurrentPath.bind(this);
        this.getCurrentPath = this.getCurrentPath.bind(this);      
    }
    
    setCurrentPath(currentPath)
    {
    	this.currentPath = currentPath;
    }
    
    getCurrentPath()
    {
    	return this.currentPath;
    }
    
    createFileRequestAndSwitchToResultTokenView(event)
    {
    	event.preventDefault();
    
    	const request = document.getElementById('file-request-name').value;
    	const fileRequest = {
    		request: request,
    		targetPath: this.currentPath,
    	};
    	
    	this.props.fileRequestClient.createFileRequest(fileRequest, (createdFileRequest) => {
    		this.setState({
    			showResultToken: true,
        		resultToken: createdFileRequest.token
    		});
    	});
    }
    
    renderContent()
    {
    	if (this.state.showResultToken) {
    		return (
    			<div>
    				<label>Sende diesen Link:</label>
	    			<pre>
	    				<a id="file-request-fulfill-link" href={ 'public/file-requests/fulfill/' + this.state.resultToken }>
	    					{ 'http://' + window.location.host + '/public/file-requests/fulfill/' + this.state.resultToken }
	    				</a>
					</pre>
    			</div>
    		);
    	} else {
	    	return (
	    		<div>
	    			<input id="file-request-name" type="text" class="form-control" placeholder="Anfrage"/>
	    			<label>Zielordner</label>
	    			<FolderListTable 
	    				fileClient={ this.props.fileClient }
	    				setCurrentPath={ this.setCurrentPath } 
	    				getCurrentPath={ this.getCurrentPath }/>
	    		</div>
	    	);
    	}
    }
    
    renderButtons()
    {
    	if (this.state.showResultToken) {
    		return (
	  			<button 
		    		type="button" 
		    		class="btn btn-secondary" 
	        		data-dismiss="modal"> 
	      			Schließen
	  			</button>
    		);
    	} else {
	    	return (
	    		<button 
		    		type="button" 
		    		class="btn btn-secondary" 
		    		onClick={ (e) => this.createFileRequestAndSwitchToResultTokenView(e) }>
	      			Erstellen
	  			</button>
	    	);
    	}
    }
    
    render()
    {    
    	return (
    		<BootstrapModal id={ this.props.id } 
    			headline={ 'Neue Dateianfrage' }
    			content={ this.renderContent() }
    			buttons={ this.renderButtons() }
    			tabIndex={ -2 }/>
        );
    }
}

export default CreateFileRequestModal 