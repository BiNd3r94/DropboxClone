import React from 'react';

class Logo extends React.Component
{
	render()
	{
		const width = this.props.width;
		const height = this.props.height;
	
		return (
			<svg id="logo" aria-label="Start" xmlns="http://www.w3.org/2000/svg" role="img" width={ width + 'px' } height={ height + 'px' } viewBox={ '0 0 ' + width + ' ' + height }><title></title><path d="M8 2.4l8 5.1-8 5.1-8-5.1 8-5.1zm16 0l8 5.1-8 5.1-8-5.1 8-5.1zM0 17.7l8-5.1 8 5.1-8 5.1-8-5.1zm24-5.1l8 5.1-8 5.1-8-5.1 8-5.1zM8 24.5l8-5.1 8 5.1-8 5.1-8-5.1z"></path></svg>
		);
	}
}

export default Logo;