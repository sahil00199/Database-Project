
function goBack(){ 
	window.location.replace("InstructorHome");
}


$(document).ready(function() {
    document.getElementById("heading").innerHTML =
        "Instructor Section Statistics";
    $.ajax({
        type: "GET",
        url: "GetInstructorSectionStats",
        
        success: function(data){
        	document.getElementById("content").innerHTML = data;
        }
    });   
});
