
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
			
			var answer = "<p id = ans" + v.qid + "> </p>";
			list.append(answer);
			var grade_button = "<button type=\"button\" class=\"btn-primary\" onclick=\"location.href='Grade?qzid="+ qzid + "&qid=" + v.qid + "'\">Grade Question</button>";
			list.append(grade_button);
			list.append("<div class='separator2'></div><br>");
    		$.ajax({
		        type: "GET",
		        url: "TAQuizQuesOptions",
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
//		        		window.location.replace("illegalAccess.html");
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
    document.getElementById("heading").innerHTML =  "Quiz";
    document.getElementById("content").innerHTML =
//        "<button type=\"button\" onclick=\"location.href='AllSubmissions?qzid="+ qzid +"';\" >View all submissions</button><br>" +
//        "<br><p id = \"max\"></p>" +
        "<div id = \"questions\"></div><br>";
//    $.ajax({
//        type: "GET",
//        url: "QuizMaximumMarks",
//        data: {"qzid": qzid},
//        success: function(data){
//        	var data1 = (jQuery.parseJSON(data));
//        	if(data1.status){
//	            MaxMarks(
//	                data1.data,
//	                $('#max')
//	            );
//        	}
//        	else{
//        		alert(data1.message);
////        		window.location.replace("illegalAccess.html");
//        	}
//        }
//    }); 
    questions();
});

function questions(){
	$('#questions').html('');
	$.ajax({
        type: "GET",
        url: "TAQuizQuestions",
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
        		alert(data1.message);
//        		window.location.replace("illegalAccess.html");
        	}
        }
    });   
}


	