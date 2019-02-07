import jQuery from 'jquery';

class BaseApiClient
{
	constructor()
	{
		this.baseUrl = 'http://localhost:8080/ws18-swa10/api/';
		this.defaultErrorHandler = (error) => { console.log(error) };
		
		if (new.target === BaseApiClient) {
	      throw new TypeError("Cannot construct BaseApiClient instances directly");
	    }
	}
	
	setDefaultErrorHandler(defaultErrorHandler)
	{
		this.defaultErrorHandler = defaultErrorHandler;
	}
	
	postFile(settings)
	{
		const mergedSettings = Object.assign({}, settings, { method: "POST" });
		mergedSettings.cache = false;
		mergedSettings.contentType = false;
		mergedSettings.processData = false;
		
		this.ajaxExpectingJsonResponse(mergedSettings);
	}
	
	post(settings)
	{
		const mergedSettings = Object.assign({}, settings, { method: "POST" });
		this.ajaxJsonExpectingResponse(mergedSettings);
	}
	
	get(settings)
	{
		const mergedSettings = Object.assign({}, settings, { method: "GET" });
		this.ajaxExpectingJsonResponse(mergedSettings);
	}
	
	put(settings)
	{
		const mergedSettings = Object.assign({}, settings, { method: "PUT" });
		this.ajaxJsonExpectingResponse(mergedSettings);
	}
	
	delete_(settings)
	{
		const mergedSettings = Object.assign({}, settings, { method: "DELETE" });
		this.ajaxJson(mergedSettings);
	}
	
	ajaxExpectingJsonResponse(settings)
	{
		settings.dataType = 'json';
		this.ajax(settings);
	}
	
	ajaxJson(settings)
	{
		settings.contentType = 'application/json';
		this.ajax(settings);
	}
	
	ajaxJsonExpectingResponse(settings)
	{
		settings.dataType = 'json';
		settings.contentType = 'application/json';
		this.ajax(settings);
	}
	
	ajax(settings)
	{
		const finalSettings = {
    		url: this.toApiUrl(settings.url),
    		method: settings.method
    	};
		
		if (settings.hasOwnProperty('success') && settings.success != undefined) {
			finalSettings.success = settings.success;
		}
		
		if (settings.hasOwnProperty('error') && settings.error != undefined) {
			finalSettings.error = settings.error;
		} else {
			finalSettings.error = this.defaultErrorHandler;
		}
		
		if (settings.hasOwnProperty('cache')) {
			finalSettings.cache = settings.cache;
		}
		
		if (settings.hasOwnProperty('dataType')) {
			finalSettings.dataType = settings.dataType;
		}
		
		if (settings.hasOwnProperty('contentType')) {
			finalSettings.contentType = settings.contentType;
		}
		
		if (settings.hasOwnProperty('data')) {
			if (settings.contentType == 'application/json') {
				settings.data = JSON.stringify(settings.data);
			}
			
			finalSettings.data = settings.data;
		}
		
		if (settings.hasOwnProperty('processData')) {
			finalSettings.processData = settings.processData;
		}
		
		if (settings.hasOwnProperty('token')) {
			finalSettings.beforeSend = function (xhr) {
			    xhr.setRequestHeader ("Authorization", "Bearer " + settings.token);
			};
		}
		
		finalSettings.async = (settings.hasOwnProperty('async') ? settings.async : true);
		
		jQuery.ajax(finalSettings);
	}
	
	getBinary(settings)
	{
		var xhr = new XMLHttpRequest();
		xhr.open("GET", this.toApiUrl(settings.url));
		xhr.responseType = "arraybuffer";
		xhr.setRequestHeader("Authorization", "Bearer " + settings.token);
		
		if (! settings.file.hasOwnProperty('type')) {
			settings.file.type = "text/plain";
		}

		xhr.onload = function () {
		    if (this.status === 200) {
		    	console.log(xhr.response);
		    	console.log(settings.file);
		    	
		        var blob = new Blob([xhr.response], {type: settings.file.type});
		        var objectUrl = window.URL.createObjectURL(blob);
		        const anchor = settings.anchor;

		        anchor.prop('href', objectUrl);
		        anchor.prop('download', settings.file.name);
		        anchor.get(0).click();
		        
		        window.URL.revokeObjectURL(objectUrl);
		    }
		};
		xhr.send();
	}
	
	toApiUrl(uri)
	{
		return this.baseUrl + uri;
	}
}

export default BaseApiClient;