import React from 'react';
import FolderSymbol from '../file/FolderSymbol';
import FileSymbol from '../file/FileSymbol';
import FileShareActionMenu from './FileShareActionMenu';

class FileShareList extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.state = { 
        	showRenameModeFor: null,
        	showRenameLoaderFor: null,
        	currentParentPathHash: null
    	};
    	
    	this.currentDepth = 0;
    }
    
    handleFileClick(event)
    {
    	event.preventDefault();
    	
    	const column = event.target;
    	const id = column.attributes['data-id'].value;
    	
    	let fileShare;
    	
    	this.props.fileShares.forEach((fs) => {
    		if (fs.id == id) {
    			fileShare = fs;
    		}
    	});
    	
    	this.props.fileShareClient.downloadFile(fileShare);
    }
        
    renderLoadingRow()
    {
    	return (
    		<tr><td>Loading...</td><td></td><td></td></tr>
    	);
    }
    
    renderEmptyRow()
    {
    	return (
    		<tr><td>Es sind keine Freigaben vorhanden.</td><td></td><td></td></tr>
    	);
    }
    
    renderRow(fileShare)
    {
    	let column;
    	const symbol = (fileShare.isDirectory ? <FolderSymbol /> : <FileSymbol />);
    	
    	if (this.state.showRenameModeFor == null || this.state.showRenameModeFor.hash != fileShare.path) {
			const label = (this.state.showRenameLoaderFor == fileShare.path ? 'Renaming...' : fileShare.path);
			column = <span 
						data-id={ fileShare.id } 
						data-name={ fileShare.path } 
						data-dir={ fileShare.isDirectory ? 1 : 0 } 
						class="file-link" 
						onClick={ (e) => this.handleFileClick(e) }>
						{ label }
					</span>;
		} else {
			column = <input 
						data-hash={ fileShare.path } 
						type="text" 
						defaultValue={ fileShare.path } 
						onKeyDown={ (e) => this.handleFileRename(e) }/>
		}
    
    	return (
    		<tr>
    			<td>
    				{ symbol } 
    				{ column } 
    			</td>
    			<td>
    				{ fileShare.owner.firstName + ' ' + fileShare.owner.lastName }
    			</td>
    			<td>
    				<span class="float-right">
    					<FileShareActionMenu id={ fileShare.id } />
					</span>
    			</td>
			</tr>
    	);
    }
    
    generateFileShareEntries()
    {
    	const entries = [];
    
    	if (this.props.fileShares == null) {
    		entries.push(this.renderLoadingRow());
    	} else if (this.props.fileShares.length == 0) {
    		entries.push(this.renderEmptyRow());
    	} else {
	    	this.props.fileShares.forEach((fileShare) => {
	    		entries.push(this.renderRow(fileShare));
	    	});
	 	}
    	
    	return entries;
    }

    render()
    {
    	return (
	    	<table class="table">
	    		<tr>
	    			<th>Name</th>
	    			<th>Von</th>
	    			<th><span class="float-right">Menü</span></th>
	    		</tr>
	    		{ this.generateFileShareEntries() }
	    	</table>
        );
    }
}

export default FileShareList;