import React from 'react';
import FileRequestClient from '../../api/users/FileRequestClient';

class FileRequestActionMenu extends React.Component
{
	constructor(props)
	{
        super(props);
    }

    render()
    {
    	return (
	    	<div class="dropdown file-action-menu">
				<button class="btn dropdown-toggle" 
					type="button" 
					data-toggle="dropdown" 
					aria-haspopup="true" 
					aria-expanded="false">
			    	...
			  	</button>
			  	<div class="dropdown-menu dropdown-menu-right" 
			  		aria-labelledby={ 'file-request-action-menu-' + this.props.id }>
				    { this.props.renderActionButtons(this.props.id) }
			  	</div>
			</div>
        );
    }
}

export default FileRequestActionMenu;