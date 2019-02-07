import React from 'react';

class NavBar extends React.Component
{
	render()
	{
		return (
			<span class="float-right">
				<nav id="user-nav" class="navbar navbar-expand-lg navbar-light">
				  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarDropdown" aria-controls="navbarDropdown" aria-expanded="false" aria-label="Toggle navigation">
				    <span class="navbar-toggler-icon"></span>
				  </button>
				  <div class="collapse navbar-collapse" id="navbarDropdown">
				    <ul class="navbar-nav">					  
				      <li class="nav-item dropdown">
				        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				          <img id="user-image" src="public/assets/img/faceholder.svg" />
				        </a>
				        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
				          <h6 class="dropdown-header">Account</h6>
				          <a class="dropdown-item" href="http://localhost:8080/ws18-swa10/logout">Abmelden</a>					         
				        </div>
				      </li>
				    </ul>
				  </div>
				</nav>
			</span>
		);
	}
}

export default NavBar;