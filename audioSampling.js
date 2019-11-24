const fs = require('fs');
const toWav = require('audiobuffer-to-wav');
const AudioContext = require('web-audio-api').AudioContext;
const audioContext = new AudioContext;

let resp = fs.readFileSync('sample.m4a');

audioContext.decodeAudioData(resp, buffer => {
  let wav = toWav(buffer);
  // do something with the WAV ArrayBuffer ...
});