import React from 'react';
import FolderListTable from './FolderListTable';
import BootstrapModal from '../BootstrapModal';

class SelectFolderModal extends React.Component
{
	constructor(props)
	{
        super(props);    
        
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
    
    renderContent()
    {
    	return (
    		<FolderListTable 
    			fileClient={ this.props.fileClient } 
    			setCurrentPath={ this.setCurrentPath } 
    			getCurrentPath={ this.getCurrentPath } />
    	);
    }
    
    renderButtons()
    {
    	return (
    		<button type="button" 
        		class="btn btn-secondary" 
        		data-dismiss="modal" 
        		onClick={ () => this.props.handleMoveFile(this.currentPath) }>
        		Auswählen
    		</button>
    	);
    }

    render()
    {    
    	return (
    		<BootstrapModal id={ this.props.id } 
    			headline={ 'Ziel wählen' }
    			content={ this.renderContent() }
    			buttons={ this.renderButtons() }
    			tabIndex={ -1 }/>
        );
    }
}

export default SelectFolderModal;