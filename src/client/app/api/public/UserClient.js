import BaseApiClient from '../BaseApiClient';

class UserCLient extends BaseApiClient
{
    constructor()
    {
        super();
    }

    registerUser(user, callback)
    {
        super.post({
            url: "public/users",
            data: user,
            success: callback
        });
    }
}

export default UserCLient;