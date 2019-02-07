import BaseApiClient from './BaseApiClient';

class UserCLient extends BaseApiClient
{
    constructor(token)
    {
        super();
        this.token = token;
    }

    getUserByEmail(email, callback)
    {
        super.get({
            url: "users/?email=" + email,
            token: this.token,
            success: callback
        });
    }
}

export default UserCLient;