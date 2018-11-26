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
			var removeQuestion = "<form> <button type=\"button\" class=\"btn-primary\" onclick=\"removeQuestion("+v.qid+")\" > Remove Question</button> </form>";
			list.append(removeQuestion);
			var viewta = "<p id = ta" + v.qid + "</p>";
			list.append(viewta);
			
			$.ajax({
		        type: "GET",
		        url: "ShowTaQues",
		        data: {"qzid": qzid, "qid": v.qid},
		        success: function(data){
		        	var data1 = (jQuery.parseJSON(data));
		        	if(data1.status){
			            taList(
			                data1.data,
			                $('#ta' + v.qid)
			            );
		        	}
		        	else{
		        		alert(data1.message);
		        		window.location.replace("illegalAccess.html");
		        	}
		        }
		    });
			
			
			
			var taAssign = "<p id = ta" + v.qid + "><form>" +
			" Enter TA id: <input type=\"text\" id = \"taid\" name=\"taid\">" +
			" <button type=\"button\" class=\"btn-primary\" onclick=\"addta("+v.qid+")\" >" +
			" Assign TA</button> </form> </p>";
	list.append(taAssign);
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
    list.html('');
    if(result != ''){
    	$.each(result, function(k, v) {
//    		console.log(v);
			var s = "Start time : " + v.start + "<br>" + "Duration : "+ v.duration + "<br><br>" ;
			list.append(s);
			var updateSchedule = "<p id=\"max\"> </p>" +"<form> " +
			 " Enter start time: <input type=\"text\" id = \"sttime\" name=\"sttime\" placeholder=\"YYYY-MM-DD HH:MM:SS\"><br>"+
			 " Enter duration: <input type=\"text\" id = \"dur\" name=\"dur\" placeholder=\"days HH:MM:SS\"><br>"+
			 "<button type=\"button\" class=\"btn-primary\" onclick=\"updateschedule("+qzid+")\" > Update Schedule</button> </form>";
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

function weightage(result, list)
{
    // Remove current options
    list.html('');
    if(result != ''){
    	var str = 'Weighatge:';
		$.each(result, function(k, v) {
			var w = v.weightage*100;
			str+= w + "%<br>";
        });
		list.html(str);
    }
}
$(document).ready(function() {
//	document.title = "Course:"
	currTime()
    document.getElementById("heading").innerHTML =  "Quiz";
    document.getElementById("content").innerHTML =
        "<p id = \"schedule\"></p>"+
        "<button type=\"button\" class=\"btn-primary\" onclick=\"location.href='AllSubmissions?qzid="+ qzid +"';\" >View all submissions</button>&nbsp&nbsp" +
        "<button type=\"button\" class=\"btn-primary\" onclick=\"autograde()\" >Auto grade objective questions</button><br><br>"+
        "<div id = \"weightage\"></div><br>"+
	"<div id = \"questions\"></div><br>"+
        "<button type=\"button\" class=\"btn-primary\" onclick=\"location.href='AddQuizQuestion?qzid=" + qzid + "';\" > + Add Question</button>";
    schedule();
    questions();
    $.ajax({
        type: "GET",
        url: "QuizWeightage",
        data: {"qzid": qzid},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            weightage(
	                data1.data,
	                $('#weightage')
	            );
        	}
        	else{
        		window.location.replace("illegalAccess.html");
        		console.log(data1.message);
        	}
        }
    });
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
	questions();
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
//	console.log(starttime);
//	console.log(duration);
	$.ajax({
        type: "GET",
        url: "InstructorUpdateQuizTimings",
        data: {"qzid": qzid, "start" :starttime, "duration" : duration},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            alert("Successful");
	            document.location.reload(); 
        	}
        	else{
        		alert(data1.message);
        		//window.location.replace("illegalAccess.html");
        	}
        }
    }); 
	
}

function autograde(){
	$.ajax({
        type: "GET",
        url: "AutoCorrectObjective",
        data: {"qzid": qzid},
        success: function(data){
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
	            alert("Successfully graded objective questions");
        	}
        	else{
        		alert(data1.message);
        	}
        }
    }); 
}

function addta(qid)
{
	console.log("reached here");
	var taid = document.getElementById('taid').value;
	$.ajax({
        type: "GET",
        url: "AssignTaQues",
        data: {"qzid": qzid, "qid" : qid, "taid" : taid },
        success: function(data){
        	var data1 = (jQuery.parseJSON(data));
        	console.log(data1);
        	if(data1.status){
	            alert("Successfully added TA");
        	}
        	else{
        		alert(data1.message);
        	}
        }
    });
	document.location.reload();
	}

function taList(result, list)
{
    // Remove current options
	console.log(result);
	console.log(list);
    list.html('');
    if(result != ''){
    	var str = 'Alloted TAs: <br>';
		$.each(result, function(k, v) {
			str+= "ID : "+v.id +", Name : "+v.name+ "<br>";
        });
		list.html(str);
    }
    else{
    	var str = 'No TA has been alloted yet. <br>';
    	list.html(str);
    }
}


