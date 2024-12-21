<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Reset Your Password</title>
</head>
<body>
<div style="margin-top: 10px">Greetings from overwatch, ${user.firstName}</div>
<br/>
<div>
<p>This link will expire in 24 hours. After that, you'll need to submit a new request in order to reset your password. If you don't want to reset it, simply disregard this email.</p>
<p>If you do not see an email from codeoverwatch@codeoverwatch.com check your spam folder.</p>
<p>Please paste this code - <span style="font-weight: bold">${code}</span> into the reset password page</p>
<a href="https://codeoverwatch.com/reset-password?uid=${user.id}&token=${token}">Click here</a>
</div>
</body>
</html>

