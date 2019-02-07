import React from 'react';

class BootstrapModal extends React.Component
{
	constructor(props)
	{
        super(props);        
    }
    
    render()
    {
    	return (
    		<div id={ this.props.id } 
    			class="modal fade" 
    			tabindex="{ this.prop.tabIndex }" 
    			role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title">{ this.props.headline }</h5>
			        		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          				<span aria-hidden="true">&times;</span>
		        			</button>
						</div>
				    	<div class="modal-body">
				    		{ this.props.content }		
					    </div>
					    <div class="modal-footer">
					    	{ this.props.buttons }
					    </div>
				    </div>
			  	</div>
			</div>
    	);
    }
}

export default BootstrapModal;