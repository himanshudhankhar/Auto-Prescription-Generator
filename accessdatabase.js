var MongoClient = require('mongodb').MongoClient;
 function getNamesFromData(availableNouns,nextFunc){
    var availableObjects = [];
    MongoClient.connect("mongodb://localhost:27017", function(err,client){
        var db = client.db('IITJ_ERP');
        db.collection('students',function(err,students){
        students.find({}).toArray(function(err,result){
            //  console.log('result',result.length);
            for(let i =0;i<result.length;i++){
                availableObjects.push(result[i]);
             
            }
        
      
        // console.log(availableObjects.length);
        db.collection('faculties',function(err,faculties){
            faculties.find({}).toArray(function(err,resultt){
                //  console.log('resultt',resultt.length);
                for(let g =0;g<resultt.length;g++){
                    availableObjects.push(resultt[g]);
                }
            
        
            
          let matchedNames =[];
            // console.log(availableNouns.length,availableObjects.length);
                let emailIDSet = new Set();
          for(let i=0;i<availableNouns.length;i++){
              for(let j=0;j<availableObjects.length;j++){
                  let name =  availableObjects[j]['name'];
                  let rollno = availableObjects[j]['Rollno'];

                //   console.log(name);
                  if( name.toLowerCase().includes(availableNouns[i].toLowerCase()) && !emailIDSet.has(availableObjects[j].emailID)){
                      matchedNames.push({"id":availableObjects[j]._id,"name":name,"emailID":availableObjects[j].emailID});
                      emailIDSet.add(availableObjects[j].emailID);
                    // console.log("match found",availableNouns[i]);
                    }
                  else if(rollno!==undefined &&  rollno.toLowerCase().includes(availableNouns[i].toLowerCase()) && !emailIDSet.has(availableObjects[j].emailID)){
                    //   console.log("match founc",availableNouns[i]);
                    matchedNames.push({"id":availableObjects[j]._id,"name":name,"emailID":availableObjects[j].emailID});
                    emailIDSet.add(availableObjects[j].emailID);
                }else{
                    //   console.log(name,availableNouns[i]);
                  }
              }
          }  
          
         nextFunc(matchedNames);

        });
    });
});
    });
});   
}

module.exports = {getNamesFromData};