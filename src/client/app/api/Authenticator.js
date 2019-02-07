import BaseApiClient from './BaseApiClient';

class Authenticator extends BaseApiClient
{
	constructor()
	{
		super();
	}
	
	authenticate(credentials, successCallback, errorCallback)
	{
		super.post({
			url: 'authenticate',
			data: credentials,
			success: successCallback,
			error: errorCallback
		});
	}
}

export default Authenticator;