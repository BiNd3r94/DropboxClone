import React from 'react';

class SidebarMenu extends React.Component
{
	constructor(props)
	{
        super(props);
        
        this.baseUrl = "http://localhost:8080/ws18-swa10";
    }

    render()
    {
    	return (
	    	<ul id="sidebar-menu">
	    		<li><a href={ this.baseUrl }>Dateien</a></li>
	    		<li><a href={ this.baseUrl + '/file-shares' }>Freigaben</a></li>
	    		<li><a href={ this.baseUrl + '/file-requests' }>Dateianfragen</a></li>
	    	</ul>
        );
    }
}

export default SidebarMenu;