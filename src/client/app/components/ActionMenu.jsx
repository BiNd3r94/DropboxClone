import React from 'react';

class ActionMenu extends React.Component
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
			  		aria-labelledby={ this.props.id }>
				    { this.props.renderActionButtons() }
			  	</div>
			</div>
    	);
    }
}

export default ActionMenu;