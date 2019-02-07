import jQuery from 'jquery';
import BaseApiClient from '../BaseApiClient';

class FileShareClient extends BaseApiClient
{
	constructor(user, token)
	{
		super();
		this.user = user;
		this.token = token;
	}
	
	getFileShareListAsOwner(successCallback)
	{
		super.get({
			url: 'users/' + this.user.id + '/file-shares',
			token: this.token,
			success: successCallback
		});
	}
	
	getFileShareListAsMember(successCallback)
	{
		super.get({
			url: 'users/' + this.user.id + '/file-shares/inverse',
			token: this.token,
			success: successCallback
		});
	}
	
	createFileShare(fileShare, successCallback)
	{
		super.post({
			url: 'users/' + this.user.id + '/file-shares',
			token: this.token,
			data: fileShare,
			success: successCallback
		});
	}
	
	downloadFile(fileShare)
	{
		const file = { name: fileShare.path,  };
		
		super.getBinary({
			url: 'file-shares/' + fileShare.id + '/file',
			token: this.token,
			file: file,
			anchor: jQuery('#download-link')
		});
	}
}

export default FileShareClient;