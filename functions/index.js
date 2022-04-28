const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

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
    const numWaitlisted=change.before.child('waitlist').numChildren();
    const cap=change.before.child("capacity").val();
    const date=context.params.date;
    const time=context.params.time;
    const location=context.params.location;
    const startingTime=time.slice(0,time.search("-"));
    const endingTime=time.slice(time.search("-"));
    const id=location+time+date;
    const studentID=change.after.child("id").val();
    const newRes={"cap":cap,"date":date,"startingTime":startingTime,"endingTime":endingTime,"id":id,"location":location,"time":time,"studentID":studentID};
    if(before.child("waitlist").val()===after.child("waitlist").val() && before.child("capacity").val()===after.child("capacity").val()){
        console.log("no change detected");
    }
    else if(before.child("waitlist").val()!=""&& after.child("current").val()<after.child("capacity").val()){
        const capacity=change.after.child('capacity').val();
        const newWaitlist=change.before.child("Waitlist");
        const waitlist=change.after.child("Waitlist");
        if(waitlist.hasChildren()){
            const topOfWaitlist=change.before.child("Waitlist").child("1").child("uid");
            const newWaitlist=sendNotification(topOfWaitlist);
            capacity+=1;
            topOfWaitlist.remove();

        }
        
        change.after.ref.update({"capacity":capacity,"Waitlist":waitlist});
    }
    // for(let i=2;i<=numWaitlisted;i++){
    //     const key=i.toString;
    //     change.after.child("Waitlist").update({key:(i-1)});
    // }
    admin.database().ref("/reservations/${location}${time}${date}").set(newRes)
}
)
// exports.changeDates=functions.pubsub.schedule("0 12 * * *").onRun((context) => {
//     const today = new Date();
//     const dd = String(today.getDate()).padStart(2, '0');
//     const mm = String(today.getMonth() + 1).padStart(2, '0'); 
//     const yyyy = today.getFullYear();

//     today = mm + '-' + dd + '-' + yyyy;

//     const yesterday = new Date(today);
//     yesterday.setdate(yesterday.getDate()-1);
//     const ydd = String(yesterday.getDate()).padStart(2, '0');
//     const ymm = String(yesterday.getMonth()+ 1).padStart(2, '0'); 
//     const yyyyy = yesterday.getFullYear();

//     yesterday = ymm + '-' + ydd + '-' + yyyyy;

//     const tomorrow = new Date(today);
//     tomorrow.setdate(today.getDate()+1);
//     const tdd = String(tomorrow.getDate()).padStart(2, '0');
//     const tmm = String(tomorrow.getMonth()+ 1).padStart(2, '0'); 
//     const tyyyy = tomorrow.getFullYear();
//     tomorrow = tmm + '-' + tdd + '-' + tyyyy;

//     const ref= admin.database().ref('/timeslots/TODAY: ${}');
    
    
// })

async function sendNotification(userUID){
    console.log(userUID);
    const token = admin.database().ref('/users/${userUID}/notificationToken').val();
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
