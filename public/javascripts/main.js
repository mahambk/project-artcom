$(document).ready(function() {
	$('#tags').tagsInput();
});

function displayImage(input) {
	if (input.files && input.files[0]) {
        var fileReader = new FileReader();
        fileReader.onload = function (e) {
            $('#target-image')
                .attr('src', e.target.result)
        };
        fileReader.readAsDataURL(input.files[0]);
    }
}