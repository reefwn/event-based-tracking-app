// Include modules to use
const crypto = require('crypto');
const express = require('express');
const mongodb = require('mongodb');
const ObjectId = mongodb.ObjectID;
const bodyParser = require('body-parser');

// Password encryption
const generateRandomString = (length) => {
    return crypto.randomBytes(Math.ceil(length/2)).toString('hex').slice(0,length);
};
const sha512 = (password, salt) => {
    const hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    const value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};

function saltHashPassword(userPassword) {
    const salt = generateRandomString(16);
    const passwordData = sha512(userPassword, salt);
    return passwordData;
}

function checkHashPassword(userPassword, salt) {
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}

// Get current date information
function yyyymmdd() {
    var now = new Date();
    var y = now.getFullYear().toString();
    var m = (now.getMonth() + 1).toString();
    var d = now.getDate().toString();
    (d.length == 1) && (d = '0' + d);
    (m.length == 1) && (m = '0' + m);
    var yyyymmdd = y + m + d;
    return yyyymmdd;
}

function isoDate(dateStr, timeStr) {
    var dateParts = dateStr.split('-');
    var timeParts = timeStr.split(':');
    return new Date(dateParts[2], dateParts[1]-1, dateParts[0], timeParts[0], timeParts[1], '00');
}

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

const MongoClient = mongodb.MongoClient;
const url = 'mongodb://localhost:27017';
MongoClient.connect(url, {useNewUrlParser: true}, (err, client) => {
    if(err) {

        console.log('[MongoDB ERROR]', err)
    
    } else {
        
        app.post('/register/', (reg, res, next) => {
            // Get data from post body
            const postData = reg.body;
            const plainPassword = postData.password;
            const hashData = saltHashPassword(plainPassword);
            const password = hashData.passwordHash;
            const salt = hashData.salt;
            const name = postData.name;
            const email = postData.email;
            const insertJson = {
                'name': name,
                'email': email,
                'password': password,
                'salt': salt
            }
            // Connect to dabase to 1. check for existing user 2. insert new user record
            const db = client.db('application');
            db.collection('user').find({'email':email}).count((err, number) => {
                if(number != 0)
                {
                    res.json('User already exist!');
                }
                else
                {
                    db.collection('user').insertOne(insertJson, (err, resp) => {
                        res.json('Register success');
                    });
                }
            });
        });

        app.post('/login/', (req, res, next) => {
            // Get data from post body
            const postData = req.body;
            const email = postData.email;
            const userPassword = postData.password;
            // Connect to database to 1. check for existing user 2. check for password 3. get user info
            const db = client.db('application');
            db.collection('user').find({'email':email}).count((err, number) => {
                if(number == 0)
                {
                    res.json('User not exist!');
                }
                else
                {
                    db.collection('user').findOne({'email':email}, (err, user) => {
                        const salt = user.salt;
                        const hashedPassword = checkHashPassword(userPassword, salt).passwordHash;
                        const password = user.password;
                        if(hashedPassword == password)
                        {
                            res.json(user);
                        }
                        else
                        {
                            res.json('Wrong password');
                        }
                    });
                }
            });
        });

        app.post('/createEvent/', (reg, res, next) => {
            // Get data from post body
            const postData = reg.body;
            const createdBy = postData.createdBy;
            const eventName = postData.eventName;
            const eventStartDate = postData.eventStartDate;
            const eventStartTime = postData.eventStartTime;
            const eventStart = isoDate(eventStartDate, eventStartTime);
            const eventEndDate = postData.eventEndDate;
            const eventEndTime = postData.eventEndTime;
            const eventEnd = isoDate(eventEndDate, eventEndTime);
            const locationCount = postData.locationCount;
            var locationDetail = [];
            var locationName, locationETA, locationETD, eventCode, tmp_eventCode;
            var eventDate = yyyymmdd();
            var insertJson;
            for(var i=1; i<=locationCount; i++) {
                locationName = 'eventLocation'+i.toString();
                locationETA = 'eventLocationETA'+i.toString();
                locationETD = 'eventLocationETD'+i.toString();
                locationDetail.push({'id': i, 'locationName' : postData[locationName], 'locationETA': postData[locationETA], 'locationETD': postData[locationETD]});
            }
            // Connect to database to 1. find if today code exist 2. start today code with 0001
            const db = client.db('application');
            db.collection('event').find({'eventCode':{$gt:parseInt(`${eventDate}0000`)}}).sort({'eventCode':-1}).limit(1).toArray((err, item) => {
                if(item.length > 0)
                {
                    tmp_eventCode = item[0].eventCode;
                    eventCode = tmp_eventCode + 1;
                }
                else
                {
                    eventCode = parseInt(`${eventDate}0001`);
                }
                // Prepare data to insert into db
                if(postData[locationName]) {
                    insertJson = {
                        'createdBy': ObjectId(createdBy),
                        'eventCode': eventCode,
                        'eventName': eventName,
                        'members' : [],
                        'eventStart': {'ISO': eventStart, 'date': eventStartDate, 'time': eventStartTime},
                        'eventEnd': {'ISO': eventEnd, 'date': eventEndDate, 'time': eventEndTime},
                        'eventLocations': locationDetail
                    }
                }
                else
                {
                    insertJson = {
                        'createdBy': ObjectId(createdBy),
                        'eventCode': eventCode,
                        'eventName': eventName,
                        'members' : [],
                        'eventStart': {'ISO': eventStart, 'date': eventStartDate, 'time': eventStartTime},
                        'eventEnd': {'ISO': eventEnd, 'date': eventEndDate, 'time': eventEndTime}
                    }
                }
                // Connect to database to insert new event record
                db.collection('event').insertOne(insertJson, (err, resp) => {
                    res.json('Event is created');
                });
            });
        });

        app.post('/joinEvent/', (reg, res, next) => {
            // Get data from post body
            const postData = reg.body;
            const eventCode = postData.eventCode;
            const userId = postData.userId;
            const userName = postData.userName;
            // Connect to database to 1. check if event exist 2. check if user is the owner of the event 3. insert new member record
            const db = client.db('application');
            db.collection('event').find({'eventCode': parseInt(eventCode)}).toArray((err, item) => {
                if(item.length != 0)
                {
                    if(item[0].createdBy == userId)
                    {
                        res.json('You are the owner of the event');
                    }
                    else
                    {
                        db.collection('event').updateOne({'_id': ObjectId(item[0]._id)}, {$push: {'members': {$each: [{'userId':ObjectId(userId), 'userName':userName}]}}}, (err, resp) => {
                            res.json('join completed');
                        });
                    }
                }
                else
                {
                    res.json('Inserted event code is not found');
                }
            });
        });

        app.post('/myUpcomingEvents/', (reg, res, next) => {
            // Connect to database to 1. check if user have any joined/created event 2. get events
            const db = client.db('application');
            db.collection('event').find({$or:[{'createdBy':ObjectId(reg.body.userId)}, {'members.userId':ObjectId(reg.body.userId)}]}).toArray((err, item) => {
                if(item.length != 0 || typeof item)
                {
                    res.json(item);
                }
                else
                {
                    res.json("no event found");
                }
            });
        });

        // Set server to run on default port || port 3000
        const port = process.env.PORT || 3000;
        app.listen(port, ()=> {
            console.log(`listening on ${port}`);
        });
    }
});