function buildList(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
//        		console.log(v);
            list.append("<h3> <a href = \"TASection?secid=" + v.secid + "\"> " +
            		v.courseid + " : " + v.coursename + ", " + v.semester +  ", " + v.year + 
            		"</a></h3>");
        });
    }
}

function goBack(){ 
	window.location.replace("TAHome");
}


$(document).ready(function() {
    document.getElementById("content").innerHTML =
            "<div id = \"contentList\"></div><br>";

    $.ajax({
        type: "GET",
        url: "TASections",
        success: function(data){
        	console.log(data);
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
/**
 * 
 */