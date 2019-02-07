import React from 'react';
import {render} from 'react-dom';
import LocalStorage from './api/LocalStorage';
import UserClient from './api/UserClient';
import FileClient from './api/users/FileClient';
import FileShareClient from './api/users/FileShareClient';
import PermissionClient from './api/PermissionClient';
import BreadCrumbs from './components/BreadCrumbs';
import FileListTable from './components/file/FileListTable';
import FileCreateTools from './components/file/FileCreateTools';
import AppPageContainer from './components/AppPageContainer';

class IndexApp extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = { currentPath: '' };
        this.fileListTableRef = React.createRef();
        
        this.pushToPathHeadline = this.pushToPathHeadline.bind(this);
        this.popFromPathHeadline = this.popFromPathHeadline.bind(this);
        this.getCurrentPath = this.getCurrentPath.bind(this);
        
        this.renderContent = this.renderContent.bind(this);
        this.renderControls = this.renderControls.bind(this);

        const ls = new LocalStorage(); 
        const user = ls.getUser();
        const token = ls.getToken();       
        this.userClient = new UserClient(token);
    	this.fileClient = new FileClient(user, token);
    	this.fileShareClient = new FileShareClient(user, token);
    	this.permissionClient = new PermissionClient(token);
    }
    
    pushToPathHeadline(pathToPush)
    {
    	const newCurrentPath = this.state.currentPath + '/' + pathToPush;
    	this.setState({ currentPath: newCurrentPath });
    }
    
    popFromPathHeadline()
    {
    	if (this.state.currentPath == '') return;
    	
    	const offset = this.state.currentPath.lastIndexOf("/");
    	const newCurrentPath = this.state.currentPath.substr(0, offset);
		
    	this.setState({ currentPath: newCurrentPath });
    }
    
    getCurrentPath()
    {
    	return this.state.currentPath;
    }
    
    renderContent(errorPopup)
    {
    	this.fileClient.setDefaultErrorHandler(errorPopup.show);
    	this.userClient.setDefaultErrorHandler(errorPopup.show);
    	this.fileShareClient.setDefaultErrorHandler(errorPopup.show);
    	this.permissionClient.setDefaultErrorHandler(errorPopup.show);
    
    	return (
    		<div>
    			<BreadCrumbs currentPath={ this.state.currentPath } />
    			<FileListTable 
    				ref={ this.fileListTableRef } 
    				userClient={ this.userClient } 
    				fileClient={ this.fileClient } 
    				permissionClient={ this.permissionClient }
    				fileShareClient={ this.fileShareClient } 
    				pushToPathHeadline={ this.pushToPathHeadline } 
    				popFromPathHeadline={ this.popFromPathHeadline } 
    				getCurrentPath={ this.getCurrentPath }
    				errorPopup={ errorPopup } />
    			<a id="download-link" class="hidden" href=""></a>
    		</div>
    	);
    }
    
    renderControls(errorPopup)
    {
    	this.fileClient.setDefaultErrorHandler(errorPopup.show);
    	this.userClient.setDefaultErrorHandler(errorPopup.show);
    	this.fileShareClient.setDefaultErrorHandler(errorPopup.show);
    	this.permissionClient.setDefaultErrorHandler(errorPopup.show);
    
    	return (
    		<FileCreateTools 
				fileClient={ this.fileClient } 
				getCurrentPath={ this.getCurrentPath } 
				errorPopup={ errorPopup } 
				addToFileInfoList={ (fileInfo) => this.fileListTableRef.current.addToFileInfoList(fileInfo) } />
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

render(<IndexApp/>, document.getElementById('root'));