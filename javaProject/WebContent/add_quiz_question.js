var tableStr = "<th> QuestionText </th>";

function resetError() {
	document.getElementById("error").innerHTML = "";
}
function setError(temp) {
	document.getElementById("error").style.color = "red";
	document.getElementById("error").innerHTML = temp;
}

function setPosError(temp){
	document.getElementById("error").style.color = "green";
	document.getElementById("error").innerHTML = temp;
}

function addTheQues(myqid){
//	console.log(myqid);
//	var myqid = document.getElementById("qqqid").value;
	var mytopic = document.getElementById("tttopic").value;
	var mymarks = document.getElementById("mmarks").value;
//	console.log(myqid);
//	console.log(mytopic);
//	console.log(mymarks);
	if (mymarks == ""){
		setError("Please enter maximum marks alloted");
		return;
	}
	var xhttp;
	xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
	  if (this.readyState == 4 && this.status == 200) {
		var response = JSON.parse(this.responseText);
		if(response.status){
			setPosError("Question added successfully<br></a>");
//			document.getElementById("error").style.color = "green";
		}
		else{
//			console.log
			setError(response.message);
		}
	    
	  }
	};
	var toSend = "&quizid="+qzid + "&qid=" + myqid + "&maxmarks=" + mymarks;
	xhttp.open("POST", "InsertQuizQuestion", true);
	xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	//console.log(toSend);
	xhttp.send(toSend);
}

$(document).ready(function() {
	var currentHTML =
		"<p id=\"error\" style=\"color:red\"></p>"+
	" Enter the topic: <input type=\"text\" id = \"tttopic\" name=\"tttopic\" onchange=loadQuestions()><br>"+
    " Enter the maximum marks: <input type=\"text\" id = \"mmarks\" name=\"mmarks\"><br><br>";
    
    
	var tableHTML = "<table id=\"quesTable\" cellspacing=5> " + tableStr + " </table>";
	
	document.getElementById("content").innerHTML =
        currentHTML+tableHTML;
        
//	    	"<form id=\"newSection\" onsubmit=\"createNewSection(this.course.value, this.year.value, this.semester.value)\">" +
//	        " Course ID: <input type=\"text\" id = \"course\" name=\"courseid\">"+
//	        " Year: <input type=\"text\" id = \"year\" name=\"year\">"+
//	        " Semester: <input type=\"text\" id = \"semester\" name=\"semester\">"+
//	        "<input class=\"button\" name=\"submit\" type=\"submit\" value=\"Submit\" />"+
//	        "</form>"+
//            "<div id = \"contentList\"></div><br>";
    $("#tttopic").autocomplete({
        source : function(request,response){
            var xhttp;
            xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function(){
                 if (this.readyState == 4 && this.status == 200){
                     json= JSON.parse(this.responseText);
//                     console.log(document.getElementById("tttopic").value);
                     response(json.data);
//                     console.log(document.getElementById("tttopic").value);
                 }
            }
            xhttp.open("GET", "AutoCompleteTopic?partial="+request.term + "&qzid="+qzid, true);
            xhttp.send();
        }
    });
    loadQuestions();
//    loadSections();
});

function fillThis(idToAdd){
//	console.log(textToAdd);
//	console.log(idToAdd);
//	document.getElementById("ppproblem").value = textToAdd;
	
//	document.getElementById("qqqid").value = idToAdd;
	addTheQues(idToAdd);
}

function fillTable(mydata){
//	console.log("filling now...");
//	console.log(mydata)
	
	var mytable = document.getElementById("quesTable");
	mytable.innerHTML = "";
	for (var i = 0; i < mydata.length; i++){
		var prText = mydata[i]["label"];
		var prId = mydata[i]["value"];
		var strToAdd = "<tr onclick=fillThis(\"" + prId + "\") >";
//		console.log(strToAdd);
		strToAdd += "<td> " + prText + "</td";	
		strToAdd += "</tr>";
		mytable.innerHTML += strToAdd;
	}
}

function loadQuestions(){
	var currtopic = document.getElementById("tttopic").value;
//	console.log(currtopic);
	$.ajax({
        type: "GET",
        url: "AutoCompleteQuestion",
        data: {"partial": "", "topic" : currtopic, "quizid" : qzid, "objective" : "1", "subjective" : "1"},
        success: function(data){
//        	console.log(data);
        	var data1 = (jQuery.parseJSON(data));
        	if(data1.status){
//        		console.log("this " + data1.message + "\n");
        		fillTable(
	                data1.data
	            );
        	}
        	else{
//        		console.log("this " + data1.message + "\n");
        		setError(data1.message);
//        		window.location.replace("illegalAccess.html");
        	}
        }
    }); 
}