/**
 * 
 */


$(document).ready(function() {
//	document.title = "Course:"
//	currTime()
    document.getElementById("heading").innerHTML =  "Quiz schedule. Please note down the timings and be ready";
    document.getElementById("content").innerHTML  = "<p id = \"schedule\"></p><br>";
    schedule();
});


function schedule(){
	$('#schedule').html('Schedule');
	$.ajax({
        type: "GET",
        url: "InstructorQuizTimings",
        data: {"qzid": qzid},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            scheduler(
	                data1.data,
	                $('#schedule'),
	                qzid
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });   
}


function scheduler(result, list, qzid)
{
    // Remove current options
    list.html('Schedule : <br>');
    if(result != ''){
    	$.each(result, function(k, v) {
//    		console.log(v);
			var s = "Start time:" + v.start + "<br>" + "Duration : "+ v.duration ;
			list.append(s);
		
			
        });
    }
}
