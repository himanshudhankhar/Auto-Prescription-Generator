const axios = require('axios');

axios.post('http://127.0.0.1:5000/processCommand', {
        command:'Schedule my meeting with gaurav harit',
        requestMaker:'B16CS008',
        posTaggedCommand: ["Schedule","NN","my","NN","Gaurav","NN","harit","NN"]
  })
  .then(function (response) {
     console.log(response.data);
  })
  .catch(function (error) {
    console.log(error);
  });