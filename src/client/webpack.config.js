const webpack = require('webpack');
const path = require('path');

const BUILD_DIR = path.resolve(__dirname, '../main/webapp/public/assets');
const APP_DIR = path.resolve(__dirname, 'app');

const config = {
	entry: { 
		app: [APP_DIR + '/index.jsx'],
		login: [APP_DIR + '/login.jsx'],
		fileRequest: [APP_DIR + '/fileRequest.jsx'],
		fileShare: [APP_DIR + '/fileShare.jsx'],
		fulfill: [APP_DIR + '/fulfill.jsx'],
		register: [APP_DIR + '/register.jsx']
	},
	output: {
		path: BUILD_DIR,
		filename: '[name].bundle.js'
	},
	module: {
		rules: [
			{
				test: /\.(js|jsx)$/,
				exclude: /node_modules/,
				use: ['babel-loader']
			}
	    ]
	},
	resolve: {
	    extensions: ['*', '.js', '.jsx']
	},
	devServer: {
	    contentBase: BUILD_DIR
	}
};

module.exports = config;
