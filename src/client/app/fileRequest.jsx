import React from 'react';
import {render} from 'react-dom';
import LocalStorage from './api/LocalStorage';
import FileClient from './api/users/FileClient';
import FileRequestClient from './api/users/FileRequestClient';
import FileRequestTabs from './components/fileRequest/FileRequestTabs';
import CreateFileRequestModal from './components/fileRequest/CreateFileRequestModal';
import AppPageContainer from './components/AppPageContainer';

class FileRequestApp extends React.Component
{
	constructor(props)
	{
        super(props);

    	this.renderContent = this.renderContent.bind(this);
        this.renderControls = this.renderControls.bind(this);
    	
    	const ls = new LocalStorage(); 
    	const user = ls.getUser();
    	const token = ls.getToken();
    	
    	this.fileClient = new FileClient(user, token);
    	this.fileRequestClient = new FileRequestClient(user, token);
    }
    
    renderContent(errorPopup)
    {
    	this.fileClient.setDefaultErrorHandler(errorPopup.show);
    	this.fileRequestClient.setDefaultErrorHandler(errorPopup.show);
    
    	return (
    		<div>
    			<h4 id="breadcrumbs">Aktuelle Dateianfragen</h4>
				<FileRequestTabs fileRequestClient={ this.fileRequestClient } />
    		</div>
    	);
    }
    
    renderControls(errorPopup)
    {
    	this.fileClient.setDefaultErrorHandler(errorPopup.show);
    	this.fileRequestClient.setDefaultErrorHandler(errorPopup.show);
    
    	return (
    		<div id="controls-container">
				<button 
					type="button" 
					class="btn btn-primary btn-block" 
					data-toggle="modal" data-target={ '#create-file-request-modal' }>
					Dateianfrage erstellen
				</button>
				<CreateFileRequestModal 
					id={ 'create-file-request-modal' } 
					fileClient={ this.fileClient }
					fileRequestClient={ this.fileRequestClient } />
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

render(<FileRequestApp/>, document.getElementById('root'));