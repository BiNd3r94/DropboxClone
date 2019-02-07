import BaseApiClient from './BaseApiClient';

class PermissionClient extends BaseApiClient
{
	constructor(token)
	{
		super();
		this.token = token;
	}
	
	getAllPermissions(successCallback)
	{
		super.get({
			url: 'permissions',
			token: this.token,
			success: successCallback
		});
	}
}

export default PermissionClient;