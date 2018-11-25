var n =10;

var questions = new Array(200);
var opts = new Array(200);

function optionList(result, qlist, ans, isObjective, qNum)
{
    // Remove current options
    qlist.html('');
    ans.html('');
    if(result != ''){
    	var str = "";
    	if(isObjective == 'true'){
    		opts[qNum] = new Array(n);
    		for(var i=0;i<n;i++)
    			{
    				opts[qNum][i]=0;
    			}
    		$.each(result, function(k, v) {
    			str+="<br>" +"<input type=\"checkbox\" name=\"ops\" onclick=\"selectOption("+qNum+","+k+")\">"+ v.opt ;
            });
    		str+="<br>";
    		str+="<form> <button type=\"button\" onclick=\"putResponse("+qNum+")\" > Save answer</button> </form><br>";
    		qlist.html(str);
    	}
    	else{
    		$.each(result, function(k, v) {
    			//ans.append(v.opt);
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
			var question = "<p>Q."+ k1.toString() + ": " + v.problem +"     [Marks:"+v.maxmarks.toString()+ "] </p>" +
					" <p id = op" + v.qid + " > </p>";
			list.append(question);
			var answer = "<p id = ans" + v.qid + "> </p><br>";
			questions[k] = v.qid;
			list.append(answer);
    		$.ajax({
		        type: "GET",
		        url: "StudentQuizQuesOptions",
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
    document.getElementById("content").innerHTML =
            "<div id = \"questions\"></div><br>";
    document.getElementById("heading").innerHTML =  "Quiz";
    $.ajax({
        type: "GET",
        url: "StudentQuizQuestions",
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

function putResponse(qNum)
{
	var qid = questions[qNum];
	var s="";
	for(var i=0;i<n;i++)
		{
			if(opts[qNum][i]==1)
				{
				s+=i+ " ";
				}
		}
	$.ajax({
        type: "GET",
        url: "PutResponse",
        data: {"qzid": qzid, "qid" :qid, "answer" : s},
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
	
	
}


