var n =10;
var questions = new Array(200);
var opts = new Array(200);
var iter = 1;
var sid;
var qNum;
var isobjective;
function updateMarks(){
	var qid = questions[qNum];
	console.log(sid + "dfs");
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
function getResponse(){
	var qid = questions[qNum];
	$.ajax({
        type: "GET",
        url: "QuizQuesResponseI",
        data: {"qzid": qzid, "qid": qid, "i": iter},
        success: function(data){
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
        	    var ans = "";
        	    var marks = null;
        		$.each(data1.data, function(k, v) {
        			sid = v.sid;
        			ans = v.answer;
        			marks = v.marksobtained;
        		});
        		document.getElementById("student").innerHTML = sid;
        		if(isobjective == 'true'){
        			for(var i=0;i<n;i++){
            			opts[qNum][i]=0;
            		}
        			var arr = ans.split(" ");
        			for(var i=0;i< arr.length; i++){
        				if(arr[i] != ""){
        					opts[qNum][parseInt(arr[i])]=1;
        						
        				}
        			}
        			for(var i=0;i<n;i++){
        				if(document.getElementById(qNum + "o" + i))
        					document.getElementById(qNum + "o" + i).checked = opts[qNum][i];
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

function optionList(result, qlist, ans)
{
    // Remove current options
    qlist.html('');
    ans.html('');
    if(result != ''){
    	var str = "<p>Response:</p>";
    	if(isobjective == 'true'){
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
			marks+="<button type=\"button\" onclick=\"updateMarks()\" >Update</button>";
			list.append(marks);
    		$.ajax({
		        type: "GET",
		        url: "TAQuizQuesOptions",
		        data: {"qzid": qzid, "qid": v.qid},
		        success: function(data){
		        	var data1 = (jQuery.parseJSON(data));
		        	if(data1.status){
		        		qNum = k;
		        		isobjective = v.isobjective
			            optionList(
			                data1.data,
			                $('#op' + v.qid),
			                $('#ans' + v.qid)
			            );
			            getResponse();
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
            "<h2 id= \"student\"></h2><br>"+
            "<div id = \"questions\"></div><br>"+
            "<button type=\"button\" onclick=\"previous()\" >Previous</button>"+
            "<button type=\"button\" onclick=\"next()\" >Next</button><br>";
    document.getElementById("heading").innerHTML =  "Grade Question";
    iter = 0;
    $.ajax({
        type: "GET",
        url: "TAQuizQuestion",
        data: {"qzid": qzid, "qid":qid},
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
function previous(){
	iter = Math.max(iter-1, 0);
	console.log(iter);
	getResponse();
}
function next(){
	iter = iter+1;
	console.log(iter);
	getResponse();
}

