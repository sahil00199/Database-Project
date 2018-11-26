function buildList(result, list)
{
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
            list.append("<li> <h3><a href = \"ViewSubmission?qzid=" + qzid + "&sid=" + v.sid + "\"> " + v.sid + " </a></h3></li>");
        });
    }
}

$(document).ready(function() {
    document.getElementById("content").innerHTML =
            "<ol id = \"contentList\"></ol><br>";
    document.getElementById("heading").innerHTML =
        "All Submissions";
    $('#contentList').html('');
	$.ajax({
        type: "GET",
        url: "StudentsEnrolled",
        data: {"qzid": qzid},
        success: function(data){
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            buildList(
	                data1.data,
	                $('#contentList')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        	}
        }
    });
});
