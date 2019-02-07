import React from 'react';
import {render} from 'react-dom';
import FileRequestClient from './api/users/FileRequestClient';
import FileUploadContainer from './components/file/FileUploadContainer';

class FulfillApp extends React.Component
{
	constructor(props)
	{
        super(props);
    }
    
    render()
    {
    	const token = localStorage.getItem('token');
    	const user = JSON.parse(localStorage.getItem('user'));
    	const fileRequestClient = new FileRequestClient(user, token);
    
    	return (
    		<FileUploadContainer fileRequestClient={ fileRequestClient } />
    	);
    }
}

render(<FulfillApp/>, document.getElementById('root'));