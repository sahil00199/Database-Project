var n =10;
var questions = new Array(200);
var opts = new Array(200);

function updateMarks(qNum){
	var qid = questions[qNum];
	var marks = document.getElementById("marks"+qNum).value;
	$.ajax({
        type: "GET",
        url: "UpdateMarks",
        data: {"sid": sid, "qid" : qid, "qzid" : qzid, "marks" : marks},
        success: function(data){
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
        		
        	}
        	else{
        		alert(data1.message);
        	}
        }
    });  
}
function getResponse(qNum, isObjective){
	var qid = questions[qNum];
	$.ajax({
        type: "GET",
        url: "QuizQuesResponse",
        data: {"qzid": qzid, "qid": qid, "sid": sid},
        success: function(data){
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
        	    var ans = "";
        	    var marks = null;
        		$.each(data1.data, function(k, v) {
        			ans = v.answer;
        			marks = v.marksobtained;
        		});
        		if(isObjective == 'true'){
        			var arr = ans.split(" ");
        			for(var i=0;i< arr.length; i++){
        				if(arr[i] != ""){
        					selectOption(qNum, parseInt(arr[i]));
        					document.getElementById(qNum + "o" + arr[i]).checked = true;	
        				}
        			}
        		}
        		else{
        			console.log(ans);
        			document.getElementById(""+qNum).innerHTML = ans;
        		}
        		if(marks != 'null')
        			document.getElementById("marks"+qNum).value = marks;
        	}
        	else{
        		console.log(data1.message);
        	}
        }
    });  
}

function optionList(result, qlist, ans, isObjective, qNum)
{
    // Remove current options
    qlist.html('');
    ans.html('');
    if(result != ''){
    	var str = "<p>Response:</p>";
    	if(isObjective == 'true'){
    		opts[qNum] = new Array(n);
    		for(var i=0;i<n;i++){
    			opts[qNum][i]=0;
    		}
    		
    		$.each(result, function(k, v) {
    			str+="<input type=\"checkbox\" name=\"ops\" id ="+ qNum + "o"+ k +" >"+ v.opt + "<br>" ;
            });
//    		str+="<form> <button type=\"button\" onclick=\"putResponse("+qNum+", "+ isObjective+ ")\" > Save answer</button> </form><br>";
    		qlist.html(str);
    		
    	}
    	else{
    		str+="<p id ="+ qNum +"></p>";
    		str+="<br>";
//    		str+="<form> <button type=\"button\" onclick=\"putResponse("+qNum+", "+ isObjective+ ")\" > Save answer</button> </form><br>";
    		qlist.html(str);
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
			var question = "<p>Q."+ k1.toString() + ": " + v.problem +"     [Marks:"+v.maxmarks.toString()+ "] </p>" +
					" <p id = op" + v.qid + " > </p>";
			list.append(question);
			var answer = "<p id = ans" + v.qid + "> </p>";
			questions[k] = v.qid;
			list.append(answer);
			var marks = "Marks: <input type=\"text\" size=\"4\" name=\"marks\" id=\"marks" + k + "\">";
			marks+="<button type=\"button\" onclick=\"updateMarks("+ k +")\" >Update</button>";
			list.append(marks);
			list.append("<div class='separator2'></div>")
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
			                v.isobjective,
			                k
			            );
			            getResponse(k, v.isobjective);
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
    document.getElementById("content").innerHTML =
            "<h3>Student ID : "+sid+"</h3><br>" +
            "<div id = \"questions\"></div><br>";
    document.getElementById("heading").innerHTML =  "Submission";
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
        		alert(data1.message);
        		window.location.replace("illegalAccess.html");
        	}
        }
    });   
});

function selectOption(qNum,optNum)
{
	opts[qNum][optNum] = 1- opts[qNum][optNum];
}

