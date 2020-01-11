const admin = require('firebase-admin');
const functions = require('firebase-functions')
admin.initializeApp(functions.config().firebase);
const functionTriggers = functions.region('europe-west1').firestore;
const db = admin.firestore()

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
