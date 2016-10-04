// dependencies
var passport = require('passport');
var config = require('./config.json');
var LinkedinStrategy = require('passport-linkedin-oauth2').Strategy;

// Configure the LinkedIn strategy for use by Passport.
//
// OAuth 2.0-based strategies require a `verify` function which receives the
// credential (`accessToken`) for accessing the LinkedIn API on the user's
// behalf, along with the user's profile.  The function must invoke `done`
// with a user object, which will be set at `req.user` in route handlers after
// authentication.
passport.use(new LinkedinStrategy({
        clientID: config.linkedin.clientID,
        clientSecret: config.linkedin.clientSecret,
        callbackURL: config.linkedin.callbackURL,
        //refer linkedin developer site for scope information
        scope:        [ 'r_basicprofile', 'r_emailaddress'],
        passReqToCallback: true
    },
    function(req, accessToken, refreshToken, profile, done) {
        // In this example, the user's LinkedIn profile is supplied as the user
        // record.  In a production-quality application, the LinkedIn profile should
        // be associated with a user record in the application's database, which
        // allows for account linking and authentication with other identity
        // providers.
        return done(null, profile);

    }
));

function linkedin(app){
    // GET /auth/linkedin
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  The first step in LinkedIn authentication will involve
    //   redirecting the user to linkedin.com.  After authorization, LinkedIn will
    //   redirect the user back to this application at /auth/linkedin/callback
    app.get('/auth/linkedin',
        passport.authenticate('linkedin',{ state: 'SOME STATE' }),
        function(req, res){
            // The request will be redirected to LinkedIn for authentication, so this
            // function will not be called.
        }
    );

    // GET /auth/linkedin/callback
    //   Use passport.authenticate() as route middleware to authenticate the
    //   request.  If authentication fails, the user will be redirected back to the
    //   login page.  Otherwise, the primary route function function will be called,
    //   which, in this example, will redirect the user to the account page.
    app.get('/auth/linkedin/callback',
        passport.authenticate('linkedin', { failureRedirect: '/login' }),
        function(req, res) {
            res.redirect('/account');
        }
    );
}

module.exports = linkedin;
