import jQuery from 'jquery';
import BaseApiClient from '../BaseApiClient';

class FileClient extends BaseApiClient
{
	constructor(user, token)
	{
		super();
		this.user = user;
		this.token = token;
	}
	
	getRootFiles(callback)
	{		
		super.get({
			url: 'users/' + this.user.id + '/files',
			token: this.token,
			success: callback
		});
	}
	
	getFileList(path, callback)
	{		
		super.get({
			url: 'users/' + this.user.id + '/folders/' + path,
			token: this.token,
			success: callback
		});
	}
	
	downloadFile(file)
	{	
		super.getBinary({
			url: 'users/' + this.user.id + '/files/' + file.path,
			token: this.token,
			file: file,
			anchor: jQuery('#download-link')
		});
	}
	
	downloadFolderAsZip(folder, callback)
	{
		folder.type = "application/zip";
		
		super.getBinary({
			url: 'users/' + this.user.id + '/folders/' + folder.path + '/zips',
			token: this.token,
			file: folder,
			anchor: jQuery('#download-link')
		});
	}
	
	uploadFile(uploadFile, successHandler, errorHandler)
	{
		const formData = new FormData();
		formData.append('file', uploadFile.fileInput);
		formData.append('destinationPath', uploadFile.destinationPath);
		
		super.postFile({
			url: 'users/' + this.user.id + '/files',
			token: this.token,
			data: formData,
			success: successHandler,
			error: errorHandler
		});
	}
	
	uploadFolder(uploadFolder, callback)
	{
		const formData = new FormData();
		formData.append('file', uploadFolder.fileInput);
		formData.append('destinationPath', uploadFolder.destinationPath);
		
		super.postFile({
			url: 'users/' + this.user.id + '/files/unzips',
			token: this.token,
			data: formData,
			success: callback
		});
	}
	
	createFolder(path, callback)
	{
		super.post({
			url: 'users/' + this.user.id + '/folders',
			token: this.token,
			data: { path: path },
			success: callback
		});
	}
	
	moveFile(fileMove, callback)
	{
		super.put({
			url: 'users/' + this.user.id + '/files/' + fileMove.hash,
			token: this.token,
			data: { destinationPath: fileMove.destinationPath },
			success: callback
		});
	}
	
	moveFolder(folderMove, callback)
	{
		super.put({
			url: 'users/' + this.user.id + '/folders/' + folderMove.hash,
			token: this.token,
			data: { destinationPath: folderMove.destinationPath },
			success: callback
		});
	}
	
	deleteFile(path, callback)
	{
		super.delete_({
			url: 'users/' + this.user.id + '/files/' + path,
			token: this.token,
			success: callback
		});
	}
	
	deleteFolder(path, callback)
	{
		super.delete_({
			url: 'users/' + this.user.id + '/folders/' + path,
			token: this.token,
			success: callback
		});
	}
}

export default FileClient;