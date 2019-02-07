import BaseApiClient from '../BaseApiClient';

class FileRequestClient extends BaseApiClient
{
	constructor(user, token)
	{
		super();
		this.user = user;
		this.token = token;
	}
	
	getFileRequestByToken(token, callback)
	{
		super.get({
			url: 'public/file-requests/token/' + token,
			token: this.token,
			success: callback
		});
	}
	
	createFileRequest(fileRequest, callback)
	{
		super.post({
			url: 'file-requests',
			token: this.token,
			data: {
				requester: {
					id: this.user.id
				},
				request: fileRequest.request,
				targetPath: fileRequest.targetPath
			},
			success: callback
		});
	}
	
	getAllOpen(callback)
	{
		this.getAllByStatus('open', callback);
	}
	
	getAllClosed(callback)
	{
		this.getAllByStatus('closed', callback);
	}
	
	getAllByStatus(status, callback)
	{
		super.get({
			url: 'users/' + this.user.id + '/file-requests/status/' + status,
			token: this.token,
			success: callback
		});
	}
	
	fulfill(token, fileInput, callback)
	{
		const formData = new FormData();
		formData.append('file', fileInput);
		
		super.postFile({
			url: 'public/file-requests/' + token,
			token: this.token,
			data: formData,
			success: callback
		});
	}
	
	updateFileRequest(fileRequest, callback)
	{
		super.put({
			url: 'users/' + this.user.id + '/file-requests/' + fileRequest.id,
			token: this.token,
			data: fileRequest,
			success: callback
		});
	}
	
	closeFileRequest(id, callback)
	{
		this.setFileRequestOpenStatus(id, false, callback);
	}
	
	openFileRequest(id, callback)
	{
		this.setFileRequestOpenStatus(id, true, callback);
	}
	
	setFileRequestOpenStatus(id, status, callback)
	{
		super.put({
			url: 'users/' + this.user.id + '/file-requests/' + id + '/status',
			token: this.token,
			data: { open: status },
			success: callback
		});
	}
};

export default FileRequestClient;