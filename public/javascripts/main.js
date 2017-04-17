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

function displayImage2(input) {
	if (input.files && input.files[0]) {
        var fileReader = new FileReader();
        fileReader.onload = function (e) {
            $('#target-image2')
                .attr('src', e.target.result)
        };
        fileReader.readAsDataURL(input.files[0]);
    }
}

function displayVideoFileName(input) {
    var path = $('#videoUpload').val()
    var fileName = path.split("\\").pop(-1);
    $('#videoFileName').append(fileName)
    //alert(fileName);
}