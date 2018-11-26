var tableStr = "<th> QuestionText </th>";

function resetError() {
	document.getElementById("errored").innerHTML = "";
}
function setError(temp) {
	document.getElementById("errored").style.color = "red";
	document.getElementById("errored").innerHTML = temp;
}

function setPosError(temp){
	document.getElementById("errored").style.color = "green";
	document.getElementById("errored").innerHTML = temp;
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
	" Topic filter: <input type=\"text\" id = \"tttopic\" name=\"tttopic\"><br>"+
    " Question marks: <input type=\"text\" id = \"mmarks\" name=\"mmarks\"><br>" +
    " <button class=\"btn-primary\" onclick=\"loadQuestions()\"> Filter </button><br><br>" +
    "<br><br>";
    
    var tableHTML = "<div class=\"limiter\">\n" + 
	"		<div class=\"container-table100\">\n" + 
	"			<div class=\"wrap-table100\">\n" + 
	"				<div class=\"table100 ver1 m-b-110\">\n" + 
	"					<div class=\"table100-head\">";
	tableHTML += "<table> <thead><tr class=\"row100 head\">\n <th class=\"cell100 column1\">Questions</th></tr></thead></table></div>\n";// + tableStr + " </table>";
	tableHTML += "<div class=\"table100-body js-pscroll\">\n" + 
					"						<table  id = \"quesTable1\">\n" + 
					"							<tbody id = \"quesTable\">" + "</tbody>\n" + 
					"						</table>\n" + 
					"					</div>\n" + 
					"				</div></div></div>";
	console.log(tableHTML)
	document.getElementById("content").innerHTML =
        currentHTML+tableHTML;
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
		var strToAdd = "<tr  class=\"row100 body\" onclick=fillThis('" + prId + "') >";
//		console.log(strToAdd);
		strToAdd += "<td  class=\"cell100 column1\"> " + prText + "</td>";	
		strToAdd += "</tr>";
		mytable.innerHTML += strToAdd;
	}

	console.log(mytable.innerHTML);
	console.log(document.getElementById("quesTable1").innerHTML);
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