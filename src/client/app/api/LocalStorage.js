class LocalStorage
{
	storeToken(token)
	{
		localStorage.setItem('token', token);
	}
	
	storeUser(user)
	{
		const serializedUser = JSON.stringify(user);
		localStorage.setItem('user', serializedUser);
	}
	
	getToken()
	{
		return localStorage.getItem('token');
	}
	
	getUser()
	{
		const user = localStorage.getItem('user');
		return JSON.parse(user);
	}
};

export default LocalStorage;