// dependencies
var passport = require('passport');
var config = require('./config.json');
var TwitterStrategy = require('passport-twitter').Strategy;

// Configure the Twitter strategy for use by Passport.
//
// OAuth 2.0-based strategies require a `verify` function which receives the
// credential (`accessToken`) for accessing the Twitter API on the user's
// behalf, along with the user's profile.  The function must invoke `done`
// with a user object, which will be set at `req.user` in route handlers after
// authentication.
passport.use(new TwitterStrategy({
        consumerKey: config.twitter.clientID,
        consumerSecret: config.twitter.clientSecret,
        callbackURL: config.twitter.callbackURL
    },
    function(accessToken, refreshToken, profile, done) {
        // In this example, the user's Twitter profile is supplied as the user
        // record.  In a production-quality application, the Twitter profile should
        // be associated with a user record in the application's database, which
        // allows for account linking and authentication with other identity
        // providers.
        return done(null, profile);
    }
));

function twitter(app){
    // GET /auth/twitter
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  The first step in Twitter authentication will involve
    //   redirecting the user to twitter.com.  After authorization, Twitter will
    //   redirect the user back to this application at /auth/twitter/callback
    app.get('/auth/twitter',
        passport.authenticate('twitter'),
        function(req, res){
            // The request will be redirected to Twitter for authentication, so this
            // function will not be called.
        }
    );
    // GET /auth/twitter/callback
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  If authentication fails, the user will be redirected back to the
    //   login page.  Otherwise, the primary route function function will be called,
    //   which, in this example, will redirect the user to the account page.
    app.get('/auth/twitter/callback',
        passport.authenticate('twitter', { failureRedirect: '/login' }),
        function(req, res) {
            res.redirect('/account');
        }
    );
}

module.exports = twitter;
