$( function() {
    
    $( "form[action='create']" ).submit( function( event ) {
        
        var form = $(this);
        
        jQuery.post( form.attr( "action" ), $( "#create-value" ).val(), function( token ) {
            $( ".response", form ).empty().append( $("<div>").text( token ) ).qrcode( {
                typeNumber: 10,
                correctLevel: 0,
                text: token
            } );
        }, "text" );
        
        event.preventDefault();

    } );

    $( "form[action='merge']" ).submit( function( event ) {
        
        var form = $(this);

        jQuery.post( $(this).attr( "action" ), $(this).serialize(), function( dollar ) {

            $( ".response", form ).empty().append( $("<div>").text( dollar ) ).qrcode( {
                typeNumber: 10,
                correctLevel: 0,
                text: dollar
            } );

        }, "text" );

        event.preventDefault();
        
    } );

    $( "form[action='value']" ).submit( function( event ) {
        
        var form = $(this);

        jQuery.get( $(this).attr( "action" ), $(this).serialize(), function( value ) {
            $( ".response", form ).empty().text( value );
        }, "text" );
        
        event.preventDefault();
        
    } );

    $( "form[action='split']" ).submit( function( event ) {
        
        var form = $(this);

        jQuery.post( $(this).attr( "action" ), $(this).serialize(), function( dollars ) {

            $( ".response", form ).empty();

            for( var i = 0; i < dollars.length; i++ ) {
                var dollar = JSON.stringify( dollars[i] );
                $( ".response", form ).append( $("<div>").text( dollar ) ).qrcode( {
                    typeNumber: 10,
                    correctLevel: 0,
                    text: dollar
                } );
            }

        } );
        
        event.preventDefault();
        
    } );

} );
