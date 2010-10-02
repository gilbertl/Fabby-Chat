Fabby Chat
==========

An Android Facebook Chat application with a horizontal scrolling conversation tabs.

Works partially (the conversation tabs work and you can chat a bit with friends); stopped working on it.

Dependencies include the Asmack library and Facebook Android SDK.

At the time of this writing, Facebook Chat servers and API still require a "session_key" which the Facebook SDK doesn't offer. I had to tweak the SDK a little so it would extract the session key from URLs and pass it along to my listener.

License
--------
See attached MIT license.
