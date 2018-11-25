/**
 * 
 */function buildList(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<h3> <a href = \"TAQuiz?qzid=" + v.qzid + "\"> " + v.qzname + " </a></h3>");
        });
    }
}
function goBack(){ 
	window.location.replace("TAHome");
}

$(document).ready(function() {
//	document.title = "Course:"
    document.getElementById("content").innerHTML =
            "<div id = \"contentList\"></div><br>";
    document.getElementById("heading").innerHTML =
        "Section Details";
//    document.getElementById("content").innerHTML +=
//        "<button type=\"button\" onclick=window.location.replace(\"illegalAccess.html\")>Login</button>";
    
    loadQs();
});

function loadQs(){
	$('#contentList').html('');
	$.ajax({
        type: "GET",
        url: "TAQuizzes",
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
}

$(document).ready(function() {
    $("#linker").click(function(e)
    {
        e.preventDefault();
//        showCreateQuiz();
    }  );
});

