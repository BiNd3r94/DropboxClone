import React from 'react';

class RegisterForm extends React.Component
{
    constructor(props)
    {
        super(props);
    }

    handleSubmit(event)
    {
        event.preventDefault();

        const user = {};
        const formFields = event.target.getElementsByTagName('input');

        Array.from(formFields).forEach((field) => {
            user[field.name] = field.value;
        });

        this.props.userClient.registerUser(user, () => {
            this.props.callback();
        });
    }

    render()
    {
        return (
            <form id="register-form" method="POST" onSubmit={ (e) => this.handleSubmit(e) }>
				<input id="firstName" class="form-control" type="text" name="firstName" placeholder="Vorname"/>
                <input id="lastName" class="form-control" type="text" name="lastName" placeholder="Nachname"/>
                <input id="email" class="form-control" type="email" name="email" placeholder="E-Mail"/>
                <input id="username" class="form-control" type="text" name="username" placeholder="Benutzername"/>
				<input id="password" class="form-control" type="password" name="password" placeholder="Passwort"/>

                <button class="btn btn-primary btn-block" type="submit">Registrieren</button>

            </form>
        );
    }
}

export default RegisterForm;