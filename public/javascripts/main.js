$(document).ready(function() {
	$('#tags').tagsInput();
	$('#divTypeDemo').hide();
	$('#divTypeProgress').hide();
	$('#divTypeSketch').hide();
	$('.radioType').click(function() {
		checkType(this);
	});
});

function checkType(element) {
	if(element.id == 'radioTypeNone') {
		$('#divTypeDemo').hide();
		$('#divTypeProgress').hide();
		$('#divTypeSketch').hide();
	}

	if(element.id == 'radioTypeDemo') {
		$('#divTypeDemo').show();
		$('#divTypeProgress').hide();
		$('#divTypeSketch').hide();
	}

	if(element.id == 'radioTypeProgress') {
		$('#divTypeDemo').hide();
		$('#divTypeProgress').show();
		$('#divTypeSketch').hide();
	}

	if(element.id == 'radioTypeSketch') {
		$('#divTypeDemo').hide();
		$('#divTypeProgress').hide();
		$('#divTypeSketch').show();
	}
}

function displayImage(input) {
	if (input.files && input.files[0]) {
        var fileReader = new FileReader();
        reader.onload = function (e) {
            $('#profile-image')
                .attr('src', e.target.result)
        };
        reader.readAsDataURL(input.files[0]);
    }
}


/*
$(function() {

	if($('#radioTypeNone').is(':checked')){
		$('#divTypeDemo').hide();
		$('#divTypeProgress').hide();
		$('#divTypeSketch').hide();
	}

	if($('#radioTypeDemo').is(':checked')){
		$('#divTypeDemo').show();
		$('#divTypeProgress').hide();
		$('#divTypeSketch').hide();
	}

	if($('#radioTypeProgress').is(':checked')){
		$('#divTypeDemo').hide();
		$('#divTypeProgress').show();
		$('#divTypeSketch').hide();
	}

	if($('#radioTypeSketch').is(':checked')){
		$('#divTypeDemo').hide();
		$('#divTypeProgress').hide();
		$('#divTypeSketch').show();
	}
});*/