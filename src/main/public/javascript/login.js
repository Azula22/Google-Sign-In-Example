const MAIN_PAGE = "http://localhost:8080";
const LOGIN_PAGE = MAIN_PAGE + "/login";

function onSignIn(googleUser) {
    const token = googleUser.getAuthResponse().id_token;
    validateAdminOnServer(token)
}
function validateAdminOnServer(token) {
    $.ajax({
        type: "POST",
        url: LOGIN_PAGE,
        data: "token=" + token,
        contentType: "application/x-www-form-urlencoded",
        statusCode: {
            200: function () {
                window.location.replace(MAIN_PAGE)
            },
            403: function () {
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut();
                $.when(function () {
                    window.location.replace(LOGIN_PAGE);
                }).then(function () {
                    $("p#ifAuthRejectedMessage").text("You have no admin rights")
                })
            },
            500: function () {
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut();
                $.when(function () {
                    window.location.replace(LOGIN_PAGE);
                }).then(function () {
                    $("p#ifAuthRejectedMessage").text("You have no admin rights")
                })
            }
        }
    })
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut();
    window.location.replace(LOGIN_PAGE);
}