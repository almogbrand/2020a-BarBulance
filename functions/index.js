// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions')

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

const functionTriggers = functions.region('europe-west1').firestore;
const db = admin.firestore()

exports.notification = functions.firestore.document('Events/{EventId}').onCreate((snap, context) => {
	const animalType = snap.data().animalType;
	const location = snap.data().locationCity;

	db.collection("Users")
        .get()
        .then(userDocs => {
            let payload;
            var tokens = [];
           userDocs.forEach(userDoc => {
			   if(userDoc.data() !== ""){
					const token = userDoc.data().fcmtoken;
					if (token!== "") {
						tokens.push(token);
					}
				}
			});

			//console.log('token is ' + token.toString());
			payload = {
				data: {
					title: animalType + ' in ' + location + ' needs your help!',
					body: snap.data().description,
					sound: 'default'
				}
			};
            return admin.messaging().sendToDevice(tokens, payload).then(response =>{
				//console.log("Successfully sent request message to " +"\nResponse: ", response,
				//"\npayload:", payload, "\ntoken:", token);
                return response;
			}).catch(error=>{
				//console.log("Error sending notification", error);
			})

		})
		.catch(error => {
            console.log(error);
		});
		return null
	});
