var express = require('express');
var app = express();
var Connection = require('tedious').Connection;
var Request = require('tedious').Request;
var path = require('path');
var bodyParser = require('body-parser');


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

// Attempt to connect and execute queries if connection goes through
connection.connect(function(err) 
   {
     if (err) 
       {
          console.log(err)
       }
    else
       {
           app.listen(3000);
           //queryDatabase();
           
       }
   }
 );

function queryDatabase()
   { console.log('Reading rows from the Table...');

       // Read all rows from table
     request = new Request(
          "SELECT * FROM [EasyPlantsDB].[sensor_data]",
             function(err, rowCount, rows) 
                {
                    console.log(rowCount + ' row(s) returned');
                    process.exit();
                }
            );

     request.on('row', function(columns) {
        columns.forEach(function(column) {
            console.log("%s\t%s", column.metadata.colName, column.value);
         });
             });
     connection.execSql(request);
 }



/*
// Configure Azure SQL connection
var connection = new Connection(config);
//Establish Azure connection

connection.on('connect', function(err) 
   {
     if (err) 
       {
          console.log(err)
       }
    else
       {
           console.log('Connected to Azure');
           app.listen(3000);
           console.log('Server listening on port 3000');
       }
   }
 );
 */
 
app.use(bodyParser.json())

app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname+ '/myfile.html'));
});

app.post('/', function(req, res) {

    var jsondata = req.body;
    var values = [];
    
    for(var i=0; i< jsondata.length; i++)
      values.push([jsondata[i].data_id,jsondata[i].sensor_id, jsondata[i].humidity_value, jsondata[i].humidity_alert]);
    
    //Bulk insert using nested array [ [a,b],[c,d] ] will be flattened to (a,b),(c,d)
    connection.query('INSERT INTO sensor_data (data_id, sensor_id, humidity_value, humidity_alert) VALUES ?', [values], function(err,result) {
      if(err) {
         res.send('Error');
      }
     else {
         res.send('Success');
      }
});
});

var http = require('http');

var server = http.createServer(function(request, response) {

    response.writeHead(200, {"Content-Type": "text/plain"});
    response.end("Welcome to Easy Plants Rest API!");

});


var port = process.env.PORT || 1337;
server.listen(port);
