var MongoClient = require('mongodb').MongoClient;
const csv = require('csv-parser')
const fs = require('fs')
// Connect to the db

var facultyDetails =[];

fs.createReadStream('facultyDetails.csv')
  .pipe(csv())
  .on('data', (data) => facultyDetails.push({
     name:data['faculty name'],
     emailID:data['faculty emailId'] 
  }))
  .on('end', () => {
    console.log(facultyDetails);
  });

var studentDetails = [];


fs.createReadStream('student_details.csv')
  .pipe(csv())
  .on('data', (data) => studentDetails.push({
name:data.name,
emailID:data.emailID,
Rollno:data.Rollno
  }))
  .on('end', () => {
    console.log(studentDetails);
  });

  MongoClient.connect("mongodb://localhost:27017", function(err,client){
var db = client.db('voiceassistant');


    if(err) { return console.dir(err); }
    console.log(db);
   db.collection('faculties',function(err,faculties){
    faculties.insert(facultyDetails,function(error,result){
        if(error){
            console.log('error while inserting faculty Details');
        }else{
            console.log('inserted faculties');
        }
    })
   });
     db.collection('students',function(err,students){
        students.insert(studentDetails,function(error,result){
            if(error){
                console.log('error while inserting students Details');
            }else{
                console.log('inserted students');
            }
                })            
     });
    
});