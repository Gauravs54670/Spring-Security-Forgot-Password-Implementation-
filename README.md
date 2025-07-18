📧 Forgot Password via Email
1→ When a user forgets their password, they initiate a password reset by calling the POST /api/public/auth/forgot-password endpoint, providing their registered email in the request body. Since this is a public endpoint, it bypasses authentication checks.

2→ The controller checks if a user with the provided email exists in the database. If found, a secure random 6-digit numeric token is generated and stored in the user's record along with a token expiry time (e.g., 10 minutes from now).

3→ The application uses JavaMailSender to send an email to the user containing the OTP/token and instructions for resetting the password.

4→ The user then calls the PUT /api/public/auth/reset-password endpoint with the token and a new password. This request is also public and bypasses security filters.

5→ The application searches for a user with the given token. If found and the token is still valid (i.e., not expired), the password is updated (after being encoded with PasswordEncoder), and the token/expiry fields are cleared.

6→ The new password takes effect immediately. The token is one-time use only and becomes invalid after use or expiration.

