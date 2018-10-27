# EasyPlants
Ongoing Android Project using Azure, Humidity Sensors and Image Processing API 

# How to use git
https://www.git-tower.com/blog/git-cheat-sheet

Quick reminder: 
- Install git
- To setup local dev environment, use git init function command along with this repo's address, then fetch from Master
- To push code: 
git add .
git commit -m #task number brief description
#### make a pull request for peer code review. Please do not bypass this step and merge changes directly to Master as this will lead to unforseen consequences. 


# How to connect to Azure db with Android Studio

https://www.youtube.com/watch?v=WJBs0zKGqH0

See slack channel "cloudlinks" for jdbc link to the server

# Sending json to Azure db

send json here using POST: https://easyplants.azurewebsites.net/

Example of json:
'''
<!DOCTYPE html>
<html lang="en">
<head>
   <meta charset="UTF-8">
   <title>Node delivered HTML

</head>
<body>
  <div>
      <h1>Send JSON to Node</h1>
       <button onClick="sendJSON()">Send</button>
       <p id ="result">
       </p>
  </div>
<script>
var myData = [
    {
        "name": "Bill",
        "age": 20
    },
    {
        "name": "Lisa",
        "age": 40
    },
    {
        "name": "Ant",
        "age": 80
    }
   ]

function sendJSON(){

var xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
xmlhttp.onreadystatechange = function() {
   if (this.readyState == 4 && this.status == 200) {
      document.getElementById("result").innerHTML =
      this.responseText;
   }
};
xmlhttp.open("POST", "http://localhost:3000");
xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
xmlhttp.send(JSON.stringify(myData));

}

</script>
</body>
</html>
'''
