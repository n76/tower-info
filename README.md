tower-info
===============

Dirt simple Android App for displaying cell tower information and possible location using on phone database.

This app uses an extension of the lacells.db file used by the NetworkLocation component of μg Project at https://github.com/microg

You can generate and install the database from the scripts at https://github.com/n76/lacells-creator

Requirements
============

1. Android phone
2. lacells.db file installed on phone.

Building and installing app
===========================

ant debug
adb install bin/towerinfo-debug.apk

