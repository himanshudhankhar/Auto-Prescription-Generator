const csv = require('csv-parser');
const fs = require('fs');
let AllCommands=[];
fs.createReadStream('TotalCommands.csv')
  .pipe(csv())
  .on('data', (row) => {
    // console.log(row);
    newCommand = {'command': row['0'],'number':row['1']}
    AllCommands.push(newCommand);
    // console.log(newCommand);
  })
  .on('end', () => {
    // console.log('CSV file successfully processed');
  });
  const readline = require('readline');
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });
  
  rl.question('Enter a command: ', (answer) => {
    // TODO: Log the answer in a database
    expectedCommand = commandRecognizer(answer);
    console.log(`Expected command is: ${expectedCommand.command}`);
    rl.close();
  });
 function getsimilarity(expCommand,inputCommand){
expCommand = expCommand.split(' ');
foundCount=0;
totalCount = expCommand.length + inputCommand.length;
for ( i =0 ;i<inputCommand.length;i++){
    for(w = 0 ;w<expCommand.length;w++){
        if(expCommand[w]==inputCommand[i]){
            foundCount+=2;
        }
    }
}
return foundCount/totalCount;
 }
function commandRecognizer(inputCommand){
    inputCommand = inputCommand.split(' ');
    similarities =[1,1,1,1,1,1,1,1,1,1,1];
    maxSimilarity=0;
    maxSimilarityCommand=-1;
     
        for(j=0;j<AllCommands.length;j++){
            similarity = getsimilarity(AllCommands[j].command,inputCommand);
            similarities[j]=similarity
            if(maxSimilarity < similarity){
                maxSimilarity=similarity;
                maxSimilarityCommand=j;
            }
        
    }

    if(maxSimilarityCommand==-1){
        return "none";
    }else{
       return AllCommands[maxSimilarityCommand];
    }
}


module.exports =  {commandRecognizer,getsimilarity,AllCommands};