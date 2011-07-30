<h2>Split</h2> 

<form method="post" action="split"> 

	<fieldset> 

		<legend>Dollar</legend> 

		<label for="split-id">Identifier:</label> 
		<input id="split-id" type="text" name="id" value=""/> 

		<label for="split-secret">Secret:</label> 
		<input id="split-secret" type="text" name="secret" value=""/> 

	</fieldset> 

	<label for="split-value">Value:</label> 
	<input id="split-value" type="text" name="value" value=""/> 

	<button type="submit">Split</button> 

	<div class="response"></div> 

</form> 
