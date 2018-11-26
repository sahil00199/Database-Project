function buildList1(result, list)
{
    // Remove current options
    list.html('Ongoing Quizzes : Hurry!! <br>');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<h3> <a href = \"StudentQuiz?qzid=" + v.qzid + "\"> " + v.qzname + " </a></h3>");
        });
    }
}

function buildList2(result, list)
{
    // Remove current options
    list.html('Past Quizzes. Check your marks <br>');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<h3> <a href = \"StudentQuizPastHelper?qzid=" + v.qzid + "\"> " + v.qzname + " </a></h3>");
        });
    }
}

function buildList3(result, list)
{
    // Remove current options
    list.html('Future Quizzes :(. Check schedules');
    if(result != ''){
    	$.each(result, function(k, v) {
    		console.log(v);
            list.append("<h3> <a href = \"StudentQuizFuture?qzid=" + v.qzid + "\"> " + v.qzname + " </a></h3>");
        });
    }
}

function goBack(){ 
	window.location.replace("StudentHome");
}


$(document).ready(function() {
//	document.title = "Course:"
	c= currTime();
    document.getElementById("content").innerHTML =
    	"<div id = \"coursetotal\"></div><br>"+
            "<div id = \"contentListpr\"></div><br>"+
            "<div id = \"contentListp\"></div><br>"+
            "<div id = \"contentListf\"></div><br>";
    document.getElementById("heading").innerHTML =
        "Section Details";
    $.ajax({
        type: "GET",
        url: "StudentQuizzesOngoing",
        data: {"secid": secid, "time" : c},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	console.log(data1);
        	if(data1.status){
	            buildList1(
	                data1.data,
	                $('#contentListpr')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });  
    
    $.ajax({
        type: "GET",
        url: "StudentQuizzesDone",
        data: {"secid": secid,"time" : c},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	console.log(data1);
        	if(data1.status){
	            buildList2(
	                data1.data,
	                $('#contentListp')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });
    
    $.ajax({
        type: "GET",
        url: "StudentQuizzesFuture",
        data: {"secid": secid, "time" : c},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	console.log(data1);
        	if(data1.status){
	            buildList3(
	                data1.data,
	                $('#contentListf')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });
    
    $.ajax({
        type: "GET",
        url: "StudentCourseMarks",
        data: {"secid": secid},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	console.log(data1);
        	if(data1.status){
	            coursetotal(
	                data1.data,
	                $('#coursetotal')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });
});

function currTime()
{
	d = new Date()
	var  h = (d.getHours()<10?'0':'') + d.getHours();
	  var  m = (d.getMinutes()<10?'0':'') + d.getMinutes();
	  var s = (d.getSeconds()<10?'0':'') + d.getSeconds();
	  time = h + ':' + m+':'+s;
	y = new Date().toLocaleDateString();
	date = y[6]+y[7]+y[8]+y[9]+'-'+y[0]+y[1]+'-'+y[3]+y[4]
	s = date + ' '+time
	console.log(s)
	return s
}

function coursetotal(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	var str = 'Total marks in all checked questions, including weightage :';
		$.each(result, function(k, v) {
			str+= v.s + "<br>";
        });
		list.html(str);
    }
}
