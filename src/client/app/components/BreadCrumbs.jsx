import React from 'react';

class BreadCrumbs extends React.Component
{
	constructor(props)
	{
        super(props);
    }

    render()
    {
    	return (
	    	<h4 id="breadcrumbs">Meine Dateien: { this.props.currentPath }</h4>
        );
    }
}

export default BreadCrumbs;