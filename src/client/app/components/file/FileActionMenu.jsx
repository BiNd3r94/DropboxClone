import React from 'react';
import FileClient from '../../api/users/FileClient';

class FileActionMenu extends React.Component
{
	constructor(props)
	{
        super(props);
    }
    
    handleFileDeletion(event)
    {
    	event.preventDefault();
    	    	
    	if (this.props.file.directory) {
    		this.props.fileClient.deleteFolder(this.props.file.hash, () => {
	    		this.props.removeFromFileInfoList(this.props.file.hash);
	    	});
    	} else {
	    	this.props.fileClient.deleteFile(this.props.file.hash, () => {
	    		this.props.removeFromFileInfoList(this.props.file.hash);
	    	});
    	}
    }
    
    downloadFolderAsZip(event)
    {
    	event.preventDefault();
    	
    	const folder = { 
    		name: this.props.file.name, 
    		path: this.props.file.hash 
		};
    	
    	this.props.fileClient.downloadFolderAsZip(folder, () => {});
    }

    render()
    {
    	let downloadButton = "";
    	
    	if (this.props.file.directory) {
    		downloadButton = (
    			<button 
    				class="dropdown-item" 
					type="button" 
					onClick={ (e) => this.downloadFolderAsZip(e) }>
					Download als ZIP
				</button>
			);
    	}
    
    	return (
	    	<div class="dropdown file-action-menu">
				<button class="btn dropdown-toggle" 
					type="button" 
					id={ 'file-action-menu-' + this.props.index } 
					data-toggle="dropdown" 
					aria-haspopup="true" 
					aria-expanded="false">
			    	...
			  	</button>
			  	<div class="dropdown-menu dropdown-menu-right" 
			  		data-hash={ this.props.file.hash }
			  		aria-labelledby={ 'file-action-menu-' + this.props.index }>
			  		<button class="dropdown-item" 
				    	type="button" 
				    	onClick={ () => this.props.setMoveSourceFile(this.props.file) } 
				    	data-toggle="modal" data-target={ '#' + this.props.createFileShareModalId }>
				    	Freigeben
			    	</button>
			    	<button class="dropdown-item" 
			    		type="button" 
			    		onClick={ () => this.props.switchToRenameModeFor(this.props.file) }>
			    		Umbenennen
		    		</button>
				    <button class="dropdown-item" 
				    	type="button" 
				    	onClick={ () => this.props.setMoveSourceFile(this.props.file) }
				    	data-toggle="modal" data-target={ '#' + this.props.selectFolderModalId }>
				    	Verschieben
			    	</button>
				    <button class="dropdown-item" 
				    	type="button" 
				    	onClick={ (e) => this.handleFileDeletion(e) }>
				    	Löschen
			    	</button>
			    	{ downloadButton }
			  	</div>
			</div>
        );
    }
}

export default FileActionMenu;