var isObjective = 1;
var options = new Array();
var isCorrect = new Array();
var allTopics = new Array();


$(document).ready(function() {
//	var currentHTML =
//		"<p id=\"error\" style=\"color:red\"></p>"+
//	" Enter the qid: <input type=\"text\" id = \"qqqid\" name=\"qqqid\">"+
//    " Enter the problem: <input type=\"text\" id = \"ppproblem\" name=\"pproblem\">"+
//    " Enter the topic: <input type=\"text\" id = \"tttopic\" name=\"tttopic\" onchange=loadQuestions()>"+
//    " Enter the maximum marks: <input type=\"text\" id = \"mmarks\" name=\"mmarks\">"+
//    "<button type=\"button\" onclick=\"addTheQues()\">Add the question</button> ";
//    
//	var tableHTML = "<table id=\"quesTable\"> " + tableStr + " </table>";
//	
//	document.getElementById("content").innerHTML =
//        currentHTML+tableHTML;
        
//	    	"<form id=\"newSection\" onsubmit=\"createNewSection(this.course.value, this.year.value, this.semester.value)\">" +
//	        " Course ID: <input type=\"text\" id = \"course\" name=\"courseid\">"+
//	        " Year: <input type=\"text\" id = \"year\" name=\"year\">"+
//	        " Semester: <input type=\"text\" id = \"semester\" name=\"semester\">"+
//	        "<input class=\"button\" name=\"submit\" type=\"submit\" value=\"Submit\" />"+
//	        "</form>"+
//            "<div id = \"contentList\"></div><br>";
    console.log($("#newtop").autocomplete);
    console.log(document.getElementById('newtop'));
    console.log(document.getElementById('newtop').innerHTML);
	$("#newtop").autocomplete({
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
            xhttp.open("GET", "AutoCompleteTopicCreateQuestion?partial="+request.term, true);
            xhttp.send();
        }
    });
//    loadQuestions();
//    loadSections();
});





function resetError() {
	document.getElementById("error").innerHTML = "";
}
function setError(temp) {
	document.getElementById("error").style.color = "red";
	document.getElementById("error").innerHTML = temp;
}
function validateForm(){
	console.log("there is nothgin");
	var max_limit_ques = 2000;
	var max_limit_ops = 1000;
	var ques = document.getElementById("question").value;
	/* var role = document.querySelector('input[name="role"]:checked').value; */
	if(ques.length == 0){
		setError("Question is required");
		return;
	}
	resetError();
	if (isObjective == 0) {
		var ans = document.getElementById("answer").value;
		var xhttp;
		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		  if (this.readyState == 4 && this.status == 200) {
			var response = JSON.parse(this.responseText);
			if(response.status){
				document.getElementById("errored").innerHTML = "Question created successfully<br>Proceed to the <a href=\"InstructorHome\"> Home</a> Page";
				document.getElementById("errored").style.color = "green";
			}
			else{
				setError(response.message);
			}
		    
		  }
		};
		var topstring = "&tLength=" + allTopics.length;
		for (var i = 0; i < allTopics.length; i++) {
			topstring += "&topic" + i + "=" + allTopics[i];
		}
		var toSend = "&question="+ques + "&answer=" + ans + topstring;
//		console.log(toSend);
		xhttp.open("POST", "CreateSubQuestion", true);
		xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
//		console.log(toSend);
		xhttp.send(toSend);
	}
	else {
		/* var ans = getElementById("answer").value; */
		var xhttp;
		xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		  if (this.readyState == 4 && this.status == 200) {
			var response = JSON.parse(this.responseText);
			if(response.status){
				document.getElementById("error").innerHTML = "Question created successfully<br>Proceed to the <a href=\"InstructorHome\"> Home</a> Page";
				document.getElementById("error").style.color = "green";
			}
			else{
				setError(response.message);
			}
		  }
		};
		
		xhttp.open("POST", "CreateObjQuestion", true);
		xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		var topstring = "&tLength=" + allTopics.length;
		for (var i = 0; i < allTopics.length; i++) {
			topstring += "&topic" + i + "=" + allTopics[i];
		}
		var toSend = "&question=" + ques + "&length=" + options.length;
		for (var i = 0; i < options.length; i++){
			toSend += "&answer";
			toSend += i;
			toSend += "=";
			toSend += options[i];
			toSend += "&isCorrect";
			toSend += i;
			toSend += "=";
			toSend += isCorrect[i]; //0 or 1
		}
		toSend += topstring;
//		console.log(toSend);
		xhttp.send(toSend);
	}
}

function addOption(){
	var newop = document.getElementById("newop").value;
	/* console.log(newop); */
	if (newop == "")
	{
//		console.log("again");
		setError("Empty option cannot be added");	
	}
	else {
		document.getElementById("options").innerHTML += '<br> <input type="checkbox" name="ops" value="' + options.length +'" onclick="switching(' + options.length + ')"> ' + newop;
		document.getElementById("newop").value = "";
		options.push(newop);
		isCorrect.push(0);
		resetError();
	}
}

function switching(ind) {
	//console.log("out" + ind);
	isCorrect[ind] = 1 - isCorrect[ind];
}

function makeS() {
	console.log("this");
	isObjective = 0;
	document.getElementById("mainarea").innerHTML = "Answer: <input class=\"form-control\" type='text' name='answer' id='answer' maxlength='1000'>";
//	document.getElementById("options").innerHTML = "";
	/* console.log("madeS"); */
}
function makeO() {
	options.length = 0;
	isCorrect.length = 0;	
	isObjective = 1;
	document.getElementById("mainarea").innerHTML = '<div class="form-group">'+
    '<label>Add a new option</label>'+
    '<input type="text" name="newop" id="newop" maxlength="1000" class="form-control">'+
    '<!-- <textarea class="form-control" name="message" rows="8"></textarea> -->'+
'</div>'+
'<button type="button" onclick="addOption()" class="btn"> Add Options</button></p>'+
'<p id="options">Current Options:</p>;';
//	document.getElementById("adding").innerHTML = 'Add a new option <br> <input type="text" name="newop" id="newop" maxlength="1000"> <br><br><button type="button" onclick="addOption()"> Add Option</button>';
	document.getElementById("options").innerHTML = "Current Options:";
	/* console.log("madeO");	 */
}

function goBack(){ 
	window.location.replace("InstructorHome");
}

function addTopic(){
	var newtop = document.getElementById("newtop").value;
	/* console.log(newop); */
	if (newtop == "")
	{
//		console.log("again");
		setError("Empty topic cannot be added");	
		document.getElementById("newtop").value = "";
	}
	else {
		var flag = 0;
		for (var i = 0; i < allTopics.length; i++){
			if (newtop == allTopics[i]) {
				flag = 1;
				break;
			}
		}
		if (flag == 1){
			setError("Topic already added");
		}
		else {
			document.getElementById("currtopics").innerHTML += '<br> ' + newtop;
			document.getElementById("newtop").value = "";
			allTopics.push(newtop);
			resetError();
		}
	}	
}