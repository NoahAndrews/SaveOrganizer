## Testing

Parts of the integration test suite require a reddit test account.
To make those tests pass, create a new, dedicated reddit account, and
save only [this post](https://www.reddit.com/r/androiddev/comments/6bqlds/kotlin_is_officially_supported_on_android/).

We're using Custom Tabs, so just log into your test account in Chrome
(or whatever browser you're using that implements Custom Tabs) on your
testing device or emulator. For now, you need to use this login URL:
https://www.reddit.com/login.compact. I recommend allowing Chrome to
save the password.

If you don't feel like setting up the account, don't. Those tests aren't
intended to be run all of the time anyway.

