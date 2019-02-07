import React from 'react';
import FileRequestActionMenu from './FileRequestActionMenu';

class FileRequestList extends React.Component
{
	constructor(props)
	{
        super(props);
    }
        
    renderLoadingRow()
    {
    	return (
    		<tr><td>Loading...</td></tr>
    	);
    }
    
    renderEmptyRow()
    {
    	return (
    		<tr><td>Es sind keine Anfragen vorhanden.</td></tr>
    	);
    }
    
    renderRow(fileRequest)
    {
    	return (
    		<tr>
    			<td>
    				{ fileRequest.request } 
    				<span class="float-right">
    					<FileRequestActionMenu 
    						id={ fileRequest.id } 
    						renderActionButtons={ this.props.renderActionButtons } />
					</span>
    			</td>
			</tr>
    	);
    }
    
    generateFileRequestEntries()
    {
    	const entries = [];
    
    	if (this.props.fileRequests == null) {
    		entries.push(this.renderLoadingRow());
    	} else if (this.props.fileRequests.length == 0) {
    		entries.push(this.renderEmptyRow());
    	} else {
	    	this.props.fileRequests.forEach((fileRequest) => {
	    		entries.push(this.renderRow(fileRequest));
	    	});
	 	}
    	
    	return entries;
    }

    render()
    {
    	return (
	    	<table class="table">
	    		{ this.generateFileRequestEntries() }
	    	</table>
        );
    }
}

export default FileRequestList;