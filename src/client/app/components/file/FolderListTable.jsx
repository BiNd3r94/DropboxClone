import React from 'react';
import FolderSymbol from './FolderSymbol';

class FolderListTable extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.currentDepth = 0;
        this.state = { currentParentPathHash: null };
        
        this.props.fileClient.getRootFiles((rootFileInfos) => {
        	this.setState({ fileInfos: rootFileInfos });
        });
    }
    
    handleFolderClick(event)
    {
    	event.preventDefault();
    	
    	const column = event.target;
    	const hash = column.attributes['data-hash'].value;
    	const name = column.attributes['data-name'].value;
    	
    	this.currentDepth++;
    	this.props.setCurrentPath(this.props.getCurrentPath() + '/' + name);
    	this.updateFileInfoList(hash);
    }
    
    handleGoToParentClick(event)
    {
    	event.preventDefault();
    	
    	if (this.currentDepth != 0) this.currentDepth--;
    	this.updateFileInfoList(this.state.currentParentPathHash);
    }
    
    updateFileInfoList(hash)
    {
    	this.props.fileClient.getFileList(hash, (fileInfoList) => {
    		this.setState({ 
    			fileInfos: fileInfoList.fileInfos, 
    			currentParentPathHash: fileInfoList.parentHash 
			});
		});
    }
    
    renderRow(file, index)
    {
    	return (
    		<tr>
				<td data-name={ file.name } data-hash={ file.hash } class="file-link" onClick={ (e) => this.handleFolderClick(e) }>
					<FolderSymbol />
					{ file.name }
				</td>
			</tr>
    	);
    }
    
    renderLoadingRow()
    {
    	return (
    		<tr><td>Loading...</td></tr>
    	);
    }
    
    renderEmptyRow()
    {
    	return (
    		<tr><td>Dieser Ordner ist leer.</td></tr>
    	);
    }
    
    renderGoToParentRow()
    {
    	return (
    		<tr>
    			<td onClick={ (e) => this.handleGoToParentClick(e) }>
    				<FolderSymbol /> <span class="file-link">..</span>
    			</td>
			</tr>
    	);
    }
  	
  	generateListTableEntries(fileInfos)
    {
    	const entries = [];
    	
    	if (this.currentDepth > 0) {
			entries.push(this.renderGoToParentRow());
		}
    	
    	if (fileInfos == undefined) {
    		entries.push(this.renderLoadingRow());
    	} else if (fileInfos.length == 0) {
    		entries.push(this.renderEmptyRow());
    	} else {
			fileInfos.forEach((fileInfo, index) => {
				if (fileInfo.directory) {
					entries.push(this.renderRow(fileInfo, index));
				}
			});
		}
    	
    	return entries;
    }
    
    render()
    {
    	return (
    		<table id="files-table" class="table">
    			{ this.generateListTableEntries(this.state.fileInfos) }
    		</table>
    	);
    }
}

export default FolderListTable;