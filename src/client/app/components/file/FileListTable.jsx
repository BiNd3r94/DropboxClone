import React from 'react';
import FileActionMenu from './FileActionMenu';
import FolderSymbol from './FolderSymbol';
import FileSymbol from './FileSymbol';
import CreateFileShareModal from '../fileShare/CreateFileShareModal';
import SelectFolderModal from './SelectFolderModal';

class FileListTable extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.sourceFile = '';
        this.currentDepth = 0;
        
        this.state = { 
        	showRenameModeFor: null,
        	showRenameLoaderFor: null,
        	currentParentPathHash: null,
        	permissionTemplates: null
    	};
    	
    	this.setMoveSourceFile = this.setMoveSourceFile.bind(this);
        this.switchToRenameModeFor = this.switchToRenameModeFor.bind(this);
        this.removeFromFileInfoList = this.removeFromFileInfoList.bind(this);
        this.handleMoveFile = this.handleMoveFile.bind(this);
        this.handleCreateFileShare = this.handleCreateFileShare.bind(this);
        
        this.props.fileClient.getRootFiles((rootFileInfos) => {
        	this.setState({ fileInfos: rootFileInfos });
        });
        this.props.permissionClient.getAllPermissions((permissions) => {
        	const permissionTemplates = { 
        		folder: { read: [], edit: [] }, 
        		file: { read: [], edit: [] } 
    		};
    		
    		console.log(permissionTemplates);
        	
        	permissions.forEach((permission) => {
        	 	if (permission.isDirectory) {
        	 		if (permission.name.startsWith('READ')) {
        	 			permissionTemplates.folder.read.push(permission);
        	 		} else {
        	 			permissionTemplates.folder.edit.push(permission);
        	 		}
        	 	} else {
        	 		if (permission.name.startsWith('READ')) {
        	 			permissionTemplates.file.read.push(permission);
        	 		} else {
        	 			permissionTemplates.file.edit.push(permission);
        	 		}
        	 	}
        	});
        	
        	this.setState({ permissionTemplates: permissionTemplates });
        });
    }
    
    setMoveSourceFile(sourceFile)
    {
    	this.sourceFile = sourceFile;
    }
    
    removeFromFileInfoList(hash)
    {
    	const newFileInfoList = this.state.fileInfos.filter((f) => {
    		return f.hash != hash;
    	});
    	
    	this.setState({ fileInfos: newFileInfoList });
    }
    
    addToFileInfoList(fileInfo)
    {
    	this.state.fileInfos.push(fileInfo);
    	this.setState({ fileInfos: this.state.fileInfos });
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
        
    handleFileClick(event)
    {
    	event.preventDefault();
    	
    	const column = event.target;
    	const hash = column.attributes['data-hash'].value;
    	const name = column.attributes['data-name'].value;
    	const parentHash = column.attributes['data-parentHash'].value;
    	const isDirectory = column.attributes['data-dir'].value;
    	
    	if (isDirectory == 1) {
    		this.currentDepth++;
    		this.props.pushToPathHeadline(name);
    		this.updateFileInfoList(hash);
    	} else {
    		this.props.fileClient.downloadFile({ name: name, path: hash });
    	}
    }
    
    handleGoToParentClick(event)
    {
    	event.preventDefault();
    	
    	if (this.currentDepth != 0) this.currentDepth--;
    	this.props.popFromPathHeadline();
    	this.updateFileInfoList(this.state.currentParentPathHash);
    }
    
    switchToRenameModeFor(file)
    {
    	this.setState({ showRenameModeFor: file });
    }
    
   	endRenameModeAndShowLoaderFor(hash)
   	{
   		this.setState({ 
        	showRenameModeFor: null,
        	showRenameLoaderFor: hash
    	});
   	}
    
    handleFileRename(event)
    {	    
    	if (event.key == "Enter") {
    		event.preventDefault();
    	
    		const newFileName = event.target.value;
    		const destinationPath = this.props.getCurrentPath() + '/' + newFileName;
    		const file = this.state.showRenameModeFor;
    		const fileMove = { hash: file.hash, destinationPath: destinationPath };
    		    		    		
			this.endRenameModeAndShowLoaderFor(fileMove.hash);
			
			if (file.directory) {
				this.props.fileClient.moveFolder(fileMove, (newFileInfo) => {
	    			this.updateFileInfoLocally(fileMove.hash, newFileInfo);
	    		});
			} else {
	    		this.props.fileClient.moveFile(fileMove, (newFileInfo) => {
	    			this.updateFileInfoLocally(fileMove.hash, newFileInfo);
	    		});
    		}
    	}
    }
    
    updateFileInfoLocally(oldHash, newFileInfo)
    {
    	this.state.fileInfos.some((fileInfo) => {
    		const match = fileInfo.hash == oldHash;
    		
    		if (match) {
    			fileInfo.name = newFileInfo.name;
    			fileInfo.hash = newFileInfo.hash;
    		}
    		
    		return match;
    	});
    	
    	this.setState({ 
    		fileInfos: this.state.fileInfos, 
    		showRenameLoaderFor: null 
		});
    }
    
    handleMoveFile(targetPath)
    {    	    	
    	const fileMove = { hash: this.sourceFile.hash, destinationPath: targetPath };
    		    		
		if (this.sourceFile.directory) {
			this.props.fileClient.moveFolder(fileMove, () => {
				this.removeFromFileInfoList(fileMove.hash);
			});
		} else {
			this.props.fileClient.moveFile(fileMove, () => {
				this.removeFromFileInfoList(fileMove.hash);
			});
		}
    }
    
    handleCreateFileShare(email, permissionTemplate)
    {
    	this.props.userClient.getUserByEmail(email, (user) => {
    		const type = this.sourceFile.directory ? 'folder' : 'file';
    		    		
    		const fileShare = {
	    		isDirectory: this.sourceFile.directory, 
	    		path: this.sourceFile.hash, 
	    		users: [ { id: user.id } ], 
	    		permissions: this.state.permissionTemplates[type][permissionTemplate]
	    	};
	    	
	    	this.props.fileShareClient.createFileShare(fileShare, () => {
	    		console.log("DONE");
	    	});
    	});
    }

    convertTimestampToDate(timestamp)
    {
        const currentDate = new Date(timestamp);
        return currentDate.toLocaleString();
    }

    renderRow(file, index)
    {
		let column;
		const symbol = (file.directory ? <FolderSymbol /> : <FileSymbol />);
		
		if (this.state.showRenameModeFor == null || this.state.showRenameModeFor.hash != file.hash) {
			const label = (this.state.showRenameLoaderFor == file.hash ? 'Renaming...' : file.name);
			column = <span 
						data-parentHash={ file.parentHash } 
						data-name={ file.name } 
						data-dir={ file.directory ? 1 : 0 } 
						data-hash={ file.hash } 
						class="file-link" 
						onClick={ (e) => this.handleFileClick(e) }>
						{ label }
					</span>;
		} else {
			column = <input 
						data-hash={ file.hash } 
						type="text" 
						defaultValue={ file.name } 
						onKeyDown={ (e) => this.handleFileRename(e) }/>
		}
	
		return (
			<tr>
				<td>
					{ symbol } { column } 
				</td>
				<td>
					{ this.convertTimestampToDate(file.lastModifiedAt) }
				</td>
				<td>
					{ file.shareCount == 0 ? 'Nur Sie' : (file.shareCount + 1) + ' Nutzer' }
				</td>
				<td>
					<span class="float-right">
						<FileActionMenu 
							fileClient={ this.props.fileClient } 
							index={ index } 
							file={ file } 
							setMoveSourceFile={ this.setMoveSourceFile }
							switchToRenameModeFor={ this.switchToRenameModeFor } 
							createFileShareModalId={ 'create-file-share-modal' }
							selectFolderModalId={ 'select-folder-modal' } 
							removeFromFileInfoList={ this.removeFromFileInfoList } />
					</span>
				</td>
			</tr>
		);
    }
    
    renderLoadingRow()
    {
    	return (
    		<tr><td>Loading...</td><td></td><td></td><td></td></tr>
    	);
    }
    
    renderEmptyRow()
    {
    	return (
    		<tr><td>Dieser Ordner ist leer.</td><td></td><td></td><td></td></tr>
    	);
    }
    
    renderGoToParentRow()
    {
    	return (
    		<tr>
    			<td onClick={ (e) => this.handleGoToParentClick(e) }>
    				<FolderSymbol /> <span class="file-link">..</span>
    			</td>
    			<td></td>
    			<td></td>
    			<td></td>
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
	    	for (let i = 0; i < fileInfos.length; i++) {
				entries.push(this.renderRow(fileInfos[i], i));
			} 
		}
    	
    	return entries;
    }
    
    render()
    {    	
    	return (
    		<div>
	    		<table id="files-table" class="table">
	    			<tr>
	    				<th>Name</th>
	    				<th>Zuletzt geändert</th>
	    				<th>Nutzer</th>
	    				<th><span class="float-right">Menü</span></th>
	    			</tr>
	    			{ this.generateListTableEntries(this.state.fileInfos) }
	    		</table>
	    		<SelectFolderModal 
	    			id={ 'select-folder-modal' } 
	    			fileClient={ this.props.fileClient } 
	    			handleMoveFile={ this.handleMoveFile }/>
    			<CreateFileShareModal 
    				id={ 'create-file-share-modal' } 
    				fileShareClient={ this.props.fileShareClient } 
    				handleCreateFileShare={ this.handleCreateFileShare } />
    		</div>
    	);
    }
}

export default FileListTable;