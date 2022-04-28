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
        const waitlist=change.after.child(waitlist);
        if(waitlist.hasChildren()){
            const topOfWaitlist=change.before.child("Waitlist").child("1").child("uid");
            const newWaitlist=sendNotification(topOfWaitlist);
            capacity+=1;
            waitlist.removeChild(topOfWaitlist);

        }
        
        change.after.ref.update({capacity:capacity,Waitlist:waitlist});
    }
    for(let i=2;i<=numWaitlisted;i++){
        const key=i.toString;
        change.after.child("Waitlist").update({key:i-1});
    }
    admin.database().ref("/reservations").set()
}
)
exports.changeDates=functions.pubsub.schedule("0 12 * * *").onRun((context) => {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); 
    var yyyy = today.getFullYear();

    today = mm + '-' + dd + '-' + yyyy;

    var yesterday = new Date(today);
    yesterday.setdate(yesterday.getDate()-1);
    var ydd = String(yesterday.getDate()).padStart(2, '0');
    var ymm = String(yesterday.getMonth()+ 1).padStart(2, '0'); 
    var yyyyy = yesterday.getFullYear();

    yesterday = ymm + '-' + ydd + '-' + yyyyy;

    tomorrow = new Date(today);
    today.setdate(today.getDate()+1);
    var tdd = String(tomorrow.getDate()).padStart(2, '0');
    var tmm = String(tomorrow.getMonth()+ 1).padStart(2, '0'); 
    var tyyyy = tomorrow.getFullYear();
    tomorrow = tmm + '-' + tdd + '-' + tyyyy;

    
})

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
