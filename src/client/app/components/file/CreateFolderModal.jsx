import React from 'react';
import BootstrapModal from '../BootstrapModal';

class CreateFolderModal extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = {};
    }
    
    createFolder(event)
    {
    	event.preventDefault();
    
    	const folderName = document.getElementById('folder-name').value;
    	const path = this.props.getCurrentPath() + '/' + folderName;
    	
    	this.props.fileClient.createFolder(path, (fileInfo) => {
    		this.props.addToFileInfoList(fileInfo);
    	});
    }
    
    renderContent()
    {
    	return (
    		<input id="folder-name" type="text" class="form-control" placeholder="Name"/>
    	);
    }
    
    renderButtons()
    {
    	return (
    		<button 
	    		type="button" 
	    		class="btn btn-secondary" 
        		data-dismiss="modal" 
	    		onClick={ (e) => this.createFolder(e) }>
      			Erstellen
  			</button>
    	);
    }

    render()
    {    
    	return (
    		<BootstrapModal id={ this.props.id } 
    			headline={ 'Ordner erstellen' }
    			content={ this.renderContent() }
    			buttons={ this.renderButtons() }
    			tabIndex={ -2 }/>
        );
    }
}

export default CreateFolderModal;