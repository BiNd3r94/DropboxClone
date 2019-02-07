import React from 'react';
import BootstrapModal from '../BootstrapModal';

class CreateFileShareModal extends React.Component
{
	constructor(props)
	{
        super(props);    
        
        this.permissionTemplate = 'read';
        
        this.prepareAndCreateFileShare = this.prepareAndCreateFileShare.bind(this);
    }
    
    switchSelectValue(event)
    {
    	event.preventDefault();
	
		const anchor = event.target;
		const fakeSelect = anchor.closest('.fakeSelect');
				
		const label = anchor.innerHTML;
		const value = anchor.attributes['data-value'].value;
				
		fakeSelect.getElementsByClassName('currentValue')[0].innerHTML = label;		
		this.permissionTemplate = value;
    }
    
    prepareAndCreateFileShare()
    {
    	const emailInput = document.getElementById('file-share-member-email');
    	    	
    	this.props.handleCreateFileShare(emailInput.value, this.permissionTemplate);
    }
    
    renderPermissionSelectLink(label, value)
    {
    	return (
    		<a 
    			data-value={ value } 
    			href="#" 
    			onClick={ (e) => this.switchSelectValue(e) }>
    			{ label }
			</a>
    	);
    }
    
    renderContent()
    {
    	return (
    		<div>
    			<input 
	    			id="file-share-member-email" 
	    			class="form-control" 
	    			name="file-share-member" 
	    			type="email" 
	    			placeholder="E-Mail oder Benutzername"/>
			</div>
    	);
    }
    
    renderButtons()
    {
    	return (
    		<div>
    			<div class="btn-group fakeSelect">
				    <button type="button" 
				    	class="btn btn-default dropdown-toggle" 
				    	data-toggle="dropdown" 
				    	aria-expanded="false">
				        <span class="currentValue">Kann betrachten</span>
				        <span class="caret"></span>
				    </button>
				    <ul class="dropdown-menu" role="menu">
				        <li>{ this.renderPermissionSelectLink('Kann betrachten', 'read') }</li>
				        <li>{ this.renderPermissionSelectLink('Kann bearbeiten', 'edit') }</li>					     
				    </ul>
				</div>
				
				<button type="button" 
	        		class="btn btn-secondary" 
	        		onClick={ this.prepareAndCreateFileShare } 
	        		data-dismiss="modal">
	        		Freigeben
	    		</button>
    		</div>
    	);
    }

    render()
    {    
    	return (
    		<BootstrapModal id={ this.props.id } 
    			headline={ 'Placeholder' }
    			content={ this.renderContent() }
    			buttons={ this.renderButtons() }
    			tabIndex={ -3 }/>
        );
    }
}

export default CreateFileShareModal;