import React from 'react';

class FileSymbol extends React.Component
{
	constructor(props)
	{
        super(props);
    }

    render()
    {
    	return (
    		<img src="public/assets/img/file_download.png" class="file-symbol" />
        );
    }
}

export default FileSymbol;