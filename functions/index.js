const functions = require("firebase-functions");
const admin = require("firebase-admin");

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
exports.notifyWaitlist=functions.database.ref('timeslots/{location}/{date}/{time}').onUpdate(async (change,context)=>
{   
    const after=change.after.val();
    const before=change.before.val();
    if(before.waitlist===after.waitlist && before.capacity===after.capacity){
        console.log("no change detected");
    }
    else{
        const capacity=change.after.val().capacity;
        const newWaitlist=change.before.val().waitlist;
        if(waitlist.hasChildren()){
            const topOfWaitlist=change.before.child("waitlist").child("1").val();
            const newWaitlist=sendNotification(topOfWaitlist);
            capacity+=1;
            newWaitlist.removeChild(topOfWaitlist);

        }
        change.after.ref.update({capacity,newWaitlist});
    }
}
)

async function sendNotification(userUID){
    console.log(userUID);
    const token = admin.database().ref('/users/${user}/notificationToken').val();
    const payload= {
        notification:{
            title:"You're in!",
            body: " A spot opened up and you're off the waitlist!"
        }
    };
    const response = await admin.messaging().sendToDevice(token,payload);
    response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          functions.logger.error(
            'Failure sending notification to',
            tokens[index],
            error
          );
        }
    });

}