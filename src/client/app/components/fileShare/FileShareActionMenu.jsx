import React from 'react';
import ActionMenu from '../ActionMenu';

class FileShareActionMenu extends React.Component
{
	constructor(props)
	{
        super(props);
    }
    
    renderActionButtons()
    {
    	return (
    		<button 
				class="dropdown-item" 
				type="button" >
    			Kann nix
    		</button>
    	);
    }
    
    render()
    {
    	return (
    		<ActionMenu 
    			id={ 'file-share-action-menu-' + this.props.id } 
    			renderActionButtons={ this.renderActionButtons } />
    	);
    }
}

export default FileShareActionMenu;