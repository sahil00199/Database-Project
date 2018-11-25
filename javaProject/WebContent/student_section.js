function buildList(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<h3> <a href = \"StudentQuiz?qzid=" + v.qzid + "\"> " + v.qzname + " </a></h3>");
        });
    }
}

function goBack(){ 
	window.location.replace("StudentHome");
}


$(document).ready(function() {
//	document.title = "Course:"
    document.getElementById("content").innerHTML =
            "<div id = \"contentList\"></div><br>";
    document.getElementById("heading").innerHTML =
        "Section Details";
    $.ajax({
        type: "GET",
        url: "StudentQuizzes",
        data: {"secid": secid},
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
