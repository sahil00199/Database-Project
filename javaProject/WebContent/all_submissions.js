function buildList(result, list)
{
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<p> <a href = \"ViewSubmission?qzid=" + qzid + "&sid=" + v.sid + "\"> " + v.sid + " </a></p>");
        });
    }
}

$(document).ready(function() {
    document.getElementById("content").innerHTML =
            "<div id = \"contentList\"></div><br>";
    document.getElementById("heading").innerHTML =
        "All Submissions";
    $('#contentList').html('');
	$.ajax({
        type: "GET",
        url: "StudentsEnrolled",
        data: {"qzid": qzid},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	console.log(data1);
        	if(data1.status){
	            buildList(
	                data1.data,
	                $('#contentList')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });
});
