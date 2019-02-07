import React from 'react';
import {render} from 'react-dom';
import LocalStorage from './api/LocalStorage';
import FileShareClient from './api/users/FileShareClient';
import FileShareList from './components/fileShare/FileShareList';
import AppPageContainer from './components/AppPageContainer';

class FileShareApp extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = { fileShares: null };
        
        this.renderContent = this.renderContent.bind(this);
        this.renderControls = this.renderControls.bind(this);
        
        const ls = new LocalStorage(); 
    	const user = ls.getUser();
    	const token = ls.getToken();
    	
    	this.fileShareClient = new FileShareClient(user, token);
    	this.fileShareClient.getFileShareListAsMember((fileShares) => {
    		this.setState({ fileShares: fileShares });
    	});
    }
    
    renderContent(errorPopup)
    {
    	this.fileShareClient.setDefaultErrorHandler(errorPopup.show);
    
    	return (
    		<div id="content-container">
    			<h4 id="headline">Freigaben</h4>
    			<FileShareList 
    				fileShareClient={ this.fileShareClient } 
    				fileShares={ this.state.fileShares } />
				<a id="download-link" class="hidden" href=""></a>
			</div>
		);
    }
    
    renderControls(errorPopup)
    {
    	this.fileShareClient.setDefaultErrorHandler(errorPopup.show);
    
    	return (
    		<div id="controls-container">
    		</div>
		);
    }
    
    render()
    {
    	return (
    		<AppPageContainer 
    			renderContent={ this.renderContent } 
    			renderControls={ this.renderControls } />
		);
    }
}

render(<FileShareApp/>, document.getElementById('root'));