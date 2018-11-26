
function goBack(){ 
	window.location.replace("InstructorHome");
}


$(document).ready(function() {
    document.getElementById("heading").innerHTML =
        "Section Statistics";
    console.log(secid);
    $.ajax({
        type: "GET",
        url: "GetInstructorSectionStats?secid=" + secid,
        
        success: function(data){
        	document.getElementById("content").innerHTML = data;
        }
    });   
});
