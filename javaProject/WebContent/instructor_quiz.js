function topicList(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	var str = 'Topics: <br>';
		$.each(result, function(k, v) {
			str+= v.topicname + "<br>";
        });
		list.html(str);
    }
}
function optionList(result, qlist, ans, isObjective)
{
    // Remove current options
    qlist.html('');
    ans.html('Answer: <br>');
    if(result != ''){
    	var str = "";
    	if(isObjective == 'true'){
    		str+="<ol>";
    		$.each(result, function(k, v) {
    			k1=k+1
    			str+="<li>" + v.opt + "</li>";
    			if(v.iscorrect =='true') {
    				ans.append(k1.toString() + "<br>");
    			}
    			
            });
    		str+="</ol>";
    		qlist.html(str);
    	}
    	else{
    		$.each(result, function(k, v) {
    			ans.append(v.opt);
            });
    	}
    }
}
function questionList(result, list, qzid)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
//    		console.log(v);
    		var k1 = k+1;
			var question = "<p>Q."+ k1.toString() + ": " + v.problem + "         [Marks:"+v.maxmarks.toString()+ "]</p>" +
					" <p id = op" + v.qid + " > </p>";
			list.append(question);
			var topics = "<p id = topic" + v.qid + "> </p>";
			list.append(topics);
			$.ajax({
		        type: "GET",
		        url: "QuestionTopic",
		        data: {"qid": v.qid},
		        success: function(data){
		        	var data1 = (jQuery.parseJSON(data));
		        	if(data1.status){
			            topicList(
			                data1.data,
			                $('#topic' + v.qid)
			            );
		        	}
		        	else{
		        		alert(data1.message);
		        		window.location.replace("illegalAccess.html");
		        	}
		        }
		    }); 
			var answer = "<p id = ans" + v.qid + "> </p>";
			list.append(answer);
			var removeQuestion = "<form> <button type=\"button\" onclick=\"removeQuestion("+v.qid+")\" > Remove Question</button> </form><br>";
			list.append(removeQuestion);
			list.append("<div class='separator2'></div><br>");
    		$.ajax({
		        type: "GET",
		        url: "InstructorQuizQuesOptions",
		        data: {"qzid": qzid, "qid": v.qid},
		        success: function(data){
		        	var data1 = (jQuery.parseJSON(data));
		        	if(data1.status){
			            optionList(
			                data1.data,
			                $('#op' + v.qid),
			                $('#ans' + v.qid),
			                v.isobjective
			            );
		        	}
		        	else{
		        		alert(data1.message);
		        		window.location.replace("illegalAccess.html");
		        	}
		        }
		    }); 
        });
    }
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
			var updateSchedule = "<form> " +
			 " Enter the start time: <input type=\"text\" id = \"sttime\" name=\"sttime\" placeholder=\"YYYY-MM-DD HH:MM:SS\">"+
			    " Enter the duration: <input type=\"text\" id = \"dur\" name=\"dur\" placeholder=\"days HH:MM:SS\">"+
					"<button type=\"button\" onclick=\"updateschedule("+qzid+")\" > Update Schedule</button> </form><br>"+ "<p id=\"max\"> </p>";
			console.log(updateSchedule);
			list.append(updateSchedule);
			$.ajax({
		        type: "GET",
		        url: "QuizMaximumMarks",
		        data: {"qzid": qzid},
		        success: function(data){
		        	var data1 = (jQuery.parseJSON(data));
		        	if(data1.status){
			            MaxMarks(
			                data1.data,
			                $('#max')
			            );
		        	}
		        	else{
		        		alert(data1.message);
		        		window.location.replace("illegalAccess.html");
		        	}
		        }
		    }); 
			
        });
    }
}

function MaxMarks(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	var str = 'Maximum marks:';
		$.each(result, function(k, v) {
			str+= v.s + "<br>";
        });
		list.html(str);
    }
}
$(document).ready(function() {
//	document.title = "Course:"
	currTime()
    document.getElementById("heading").innerHTML =  "Quiz";
    document.getElementById("content").innerHTML =
        "<p><a id=\"newQuestionQuiz\" href=\"AddQuizQuestion?qzid=" + qzid + "\"> Add Question</a></p>\n"+
        "<button type=\"button\" onclick=\"location.href='AllSubmissions?qzid="+ qzid +"';\" >View all submissions</button>" +
        "<p id = \"schedule\"></p><br>"+
        "<div id = \"questions\"></div><br>";
    schedule();
    questions();
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

function questions(){
	$('#questions').html('');
	$.ajax({
        type: "GET",
        url: "InstructorQuizQuestions",
        data: {"qzid": qzid},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            questionList(
	                data1.data,
	                $('#questions'),
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

function removeQuestion(qid)
{
	$.ajax({
	    type: "GET",
	    url: "DeleteQuizQuestion",
	    data: {"qzid": qzid, "qid" :qid},
	    success: function(data){
//	    	console.log(data);
	    	var data1 = (jQuery.parseJSON(data));
	    	if(data1.status){
	            
	    	}
	    	else{
	    		window.location.replace("illegalAccess.html");
	    		console.log(data1.message);
	    	}
	    }
	});
	document.getElementById("content").innerHTML =
        "<div id = \"questions\"></div><br>";
	document.getElementById("heading").innerHTML =  "Quiz";
	document.getElementById("heading").innerHTML +=  "<p><a id=\"newQuestionQuiz\" href=\"AddQuizQuestion?qzid=" + qzid + "\"> Add Question</a></p>";
	$.ajax({
	    type: "GET",
	    url: "InstructorQuizQuestions",
	    data: {"qzid": qzid},
	    success: function(data){
	//    	console.log(data);
	    	var data1 = (jQuery.parseJSON(data));
	    	if(data1.status){
	            questionList(
	                data1.data,
	                $('#questions'),
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

function updateschedule(qzid)
{
	var starttime = document.getElementById('sttime').value;
	var duration = document.getElementById('dur').value;
	console.log(starttime);
	console.log(duration);
	$.ajax({
        type: "GET",
        url: "InstructorUpdateQuizTimings",
        data: {"qzid": qzid, "start" :starttime, "duration" : duration},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            alert("Successful");
        	}
        	else{
        		alert(data1.message);
        		//window.location.replace("illegalAccess.html");
        	}
        }
    }); 
	document.location.reload() 
	}



