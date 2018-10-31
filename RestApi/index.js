// grab the packages we need
var express = require('express');
var app = express();
var port = process.env.PORT || 8080;

var bodyParser = require('body-parser');
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({	extended: true })); // support encoded bodies

// Database access

var Connection = require('tedious').Connection;
var Request = require('tedious').Request;

// Create connection to database
var config = 
   {
     userName: 'MatthewYu', 
     password: 'easyq1w2e3r4Q', 
     server: 'dbeasyplants.database.windows.net', 
     options: 
        {
           database: 'EasyPlantsDB' //update me
           , encrypt: true
        }
   }
var connection = new Connection(config);





// ====================================
// URL PARAMETERS =====================
// ====================================
// http://localhost:8080/api/users?id=4&token=sadsf4&geo=us
app.get('/', function(req, res) {
  res.end("Welcome to Easy Plants Rest API");
});




// ====================================
// POST PARAMETERS ====================
// ====================================

// POST http://localhost:8080/api/users
// parameters sent with 
app.post('/', function(req, res) {
	var sensor_id = req.body.sensor_id;
	var humidity_value = req.body.humidity_value;

    queryDatabase(sensor_id, humidity_value);
	//res.send(sensor_id + ' ' + humidity_value);
});

function queryDatabase(sensor_id, humidity_value)
   { console.log('Reading rows from the Table...');

    request = new Request("INSERT INTO sensor_data (sensor_id, humidity_value, data_date) values (" + String(sensor_id) + "," + String(humidity_value) + ", CURRENT_TIMESTAMP)");
    //request = new Request("INSERT INTO sensor_data (sensor_id, humidity_value) values (77,7)");
    connection.execSql(request);  
   }
   
var http = require('http');
var server = http.createServer(function(request, response) {

    response.writeHead(200, {"Content-Type": "text/plain"});
    response.end("Easy Plants Rest API");

});

// start the server
app.listen(port);
console.log('Server started! At http://localhost:' + port);
