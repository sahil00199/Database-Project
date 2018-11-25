function buildList(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<h3> <a href = \"InstructorQuiz?qzid=" + v.qzid + "\"> " + v.qzname + " </a></h3>");
        });
    }
}
function goBack(){ 
	window.location.replace("InstructorHome");
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
        url: "InstructorQuizzes",
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
        showCreateQuiz();
    }  );
});

function showCreateQuiz()
{
    var currentHTML = "<form>" +
    " Name of Quiz: <input type=\"text\" id = \"qname\" name=\"qname\"><br>"+
    " Start time: <input type=\"text\" id = \"sttime\" name=\"sttime\" placeholder=\"YYYY-MM-DD HH:MM:SS\"><br>"+
    " Duration: <input type=\"text\" id = \"dur\" name=\"dur\" placeholder=\"days HH:MM:SS\"><br>"+
    " Maximum Marks: <input type=\"text\" id = \"maxmarks\" name=\"maxmarks\"><br>"+
    " Weightage: <input type=\"text\" id = \"weightage\" name=\"weightage\"><br>" +
    "<button type=\"button\" class=\"btn\" onclick=\"createNewQuiz()\">Submit</button>"+
//    "value=\"Submit\" />"+
    "</form>";
    document.getElementById("newConvo").innerHTML = currentHTML;
}

function createNewQuiz()
{
//	console.log("here");
	var starttime = document.getElementById('sttime').value;
	var duration = document.getElementById('dur').value;
	var maxmarks = document.getElementById('maxmarks').value;
	var weightage = document.getElementById('weightage').value;
	var qzname = document.getElementById('qname').value;
	var xhttp;
    xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
         if (this.readyState == 4 && this.status == 200){
		    json_object = JSON.parse(this.responseText);
		    if(!(json_object.status)){
		        alert(json_object.message);
		    }
		    else{
		        alert("Quiz created successfully!");
		    }
		    loadQs();
         }
    }
    xhttp.open("GET", "CreateQuiz?coursen=" + qzname + "&secid="+ secid +"&starttime="+starttime+"&duration="+duration+"&maxmarks="+maxmarks+"&weightage="+weightage, true);
    xhttp.send();
    loadQs();
}