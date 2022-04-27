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
    const after=change.after;
    const before=change.before;
    const numWaitlisted=change.before.child('waitlist').getChildrenCount();
    if(before.waitlist===after.waitlist && before.capacity===after.capacity){
        console.log("no change detected");
    }
    else if(before.waitlist!=""&& after.current<after.capacity){
        const capacity=change.after.child('capacity').val();
        const newWaitlist=change.before.child(waitlist);
        if(waitlist.hasChildren()){
            const topOfWaitlist=change.before.child("Waitlist").child("1").child("uid");
            const newWaitlist=sendNotification(topOfWaitlist);
            capacity+=1;
            newWaitlist.removeChild(topOfWaitlist);

        }
        change.after.child()
        change.after.ref.update({capacity:capacity,Waitlist:newWaitlist});
    }
    for(let i=2;i<=numWaitlisted;i++){
        const key=i.toString;
        change.after.child("Waitlist").update({key:i-1});
    }
    admin.database().ref("/reservations").set()
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
exports.changeDates=functions.database.ref('')