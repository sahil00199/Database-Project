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

$(document).ready(function() {
//	document.title = "Course:"
    document.getElementById("heading").innerHTML =  "Quiz";
    document.getElementById("content").innerHTML =
        "<p><a id=\"newQuestionQuiz\" href=\"AddQuizQuestion?qzid=" + qzid + "\"> Add Question</a></p>\n"+
        "<div id = \"questions\"></div><br>";
    questions();
});

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

