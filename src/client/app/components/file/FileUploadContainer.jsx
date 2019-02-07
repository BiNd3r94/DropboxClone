import React from 'react';

class FileUploadContainer extends React.Component
{
	constructor(props)
	{
        super(props);   
        
        this.state = { 
        	showFinishedView: false,
        	showClosedRequestView: false, 
        	loading: true 
    	};
        
        const url = window.location.href;
        
    	this.token = url.substr(url.lastIndexOf('/') + 1);
        this.props.fileRequestClient.getFileRequestByToken(this.token, (fileRequest) => {
        	const showClosedRequestView = !fileRequest.open;
        	
        	this.setState({
    			showClosedRequestView: showClosedRequestView, 
    			loading: false 
    		});
        });    
    }
    
    openFileSelectDialog()
    {
    	document.getElementById('file-selector').click();
    }
    
    uploadFile(event)
    {
    	const fileInput = event.target.files[0];
    	
    	this.props.fileRequestClient.fulfill(this.token, fileInput, () => {
    		this.setState({ showFinishedView: true });
    	});
    }

    render()
    {
    	if (this.state.loading) {
    		return (
    			<section id="file-request-fulfill-container">
    				Loading...
    			</section>
    		);
    	} else if (this.state.showClosedRequestView) {
    		return (
    			<section id="file-request-fulfill-container">
    				Diese Anfrage wurde bereits geschlossen.
    			</section>
    		);
    	} else if (this.state.showFinishedView) {
    		return (
    			<section id="file-request-fulfill-container">
    				<p>Datei wurde hochgeladen!</p>
    				<a class="btn btn-primary btn-block" href="http://localhost:8080/ws18-swa10">Zur Startseite</a>
    			</section>
    		);
    	} else {
	    	return (
		    	<section id="file-request-fulfill-container">
		    		<button 
		    			type="button" 
						class="btn btn-primary btn-block" 
						onClick={ (e) => this.openFileSelectDialog(e) }>
						Datei hochladen
					</button>
		    		<input 
		    			type="file" 
		    			id="file-selector" 
		    			class="hidden" 
		    			name="file" 
		    			onChange={ (e) => this.uploadFile(e) }/>
		    	</section>
	        );
        }
    }
}

export default FileUploadContainer;