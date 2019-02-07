import React from 'react';
import CreateFolderModal from './CreateFolderModal';

class FileCreateTools extends React.Component
{
	constructor(props)
	{
        super(props);
    }
    
    openFileSelectDialog(event)
    {
    	event.preventDefault();
    	
    	document.getElementById('file-selector').click();
    }
    
    openZipFileSelectDialog(event)
    {
    	event.preventDefault();
    	
    	document.getElementById('zip-selector').click();
    }
    
    uploadFile(event)
    {
    	const uploadFile = this.getFileInputFromEvent(event);
    	
    	this.props.fileClient.uploadFile(uploadFile, (fileInfo) => {
    		this.props.addToFileInfoList(fileInfo);
    	});
    }
    
    uploadFolderFromZip(event)
    {
    	const uploadFolder = this.getFileInputFromEvent(event);
    	    	
    	this.props.fileClient.uploadFolder(uploadFolder, (fileInfos) => {
    		fileInfos.forEach((fileInfo) => {
    			this.props.addToFileInfoList(fileInfo);
			});
    	});
    }
    
    getFileInputFromEvent(event)
    {
   		const fileInput = event.target.files[0];
    	
    	return { fileInput: fileInput, destinationPath: this.props.getCurrentPath() };
    }

    render()
    {
    	return (
	    	<div id="controls-container">
				<button type="button" 
					class="btn btn-primary btn-block" 
					onClick={ (e) => this.openFileSelectDialog(e) }>
					Datei hochladen
				</button>
				<button type="button" 
					class="btn btn-primary btn-block" 
					onClick={ (e) => this.openZipFileSelectDialog(e) }>
					Ordner hochladen
				</button>
				<button type="button" 
					class="btn btn-primary btn-block" 
					data-toggle="modal" data-target={ '#create-folder-modal' }>
					Ordner erstellen
				</button>
				<CreateFolderModal 
					id={ 'create-folder-modal' } 
					fileClient={ this.props.fileClient } 
					getCurrentPath={ this.props.getCurrentPath } 
					addToFileInfoList={ this.props.addToFileInfoList } />
				<input 
					type="file" 
					id="zip-selector" 
					name="folder-zip-upload" 
					onChange={ (e) => this.uploadFolderFromZip(e) } />
				<input type="file" id="file-selector" name="file-upload" onChange={ (e) => this.uploadFile(e) }/>
			</div>
        );
    }
}

export default FileCreateTools;