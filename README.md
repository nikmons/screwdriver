[![Build Status](https://travis-ci.com/NickMns/screwdriver.svg?branch=master)](https://travis-ci.com/NickMns/screwdriver)
[![made-with-python](https://img.shields.io/badge/Made%20with-Python-1f425f.svg)](https://www.python.org/)
[![GitHub contributors](https://img.shields.io/github/contributors/Naereen/StrapDown.js.svg)](https://github.com/NickMns/screwdriver/graphs/contributors/)
[![Percentage of issues still open](http://isitmaintained.com/badge/open/nickmns/screwdriver.svg)](http://isitmaintained.com/project/nickmns/screwdriver "Percentage of issues still open")
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/nickmns/screwdriver.svg)](http://isitmaintained.com/project/nickmns/screwdriver "Average time to resolve an issue")

# screwdriver
Manage/Monitor IT hardware servicing lifecycle

The repository contains a total of 3 projects.

1. **api** - A python REST API (flask)
2. **client** - Client Application (Android)
3. **Tracker** - Helper Application (Js)

# API

The API is responsible for handling user authentication/authorization
(requested by the client app) and resource serving.

## database

Persistent data storage is handled by **postgresql**.\
**SQLAlchemy** ORM is used for model/data handling and communication with the db.\
Database migrations are performed using **alembic**,

## pipenv

This project uses *pipenv* for virtual environment management.
To get started with pipenv you need to install it via pip.

```shell
cd api
pip install pipenv
```

Then following command initializes a new virtual environemnt,
that will be used for the current project,

```shell
pipenv install
```

To use the virtual environment we can either start a new shell
inside it.

```shell
pipenv shell
```

Or run single commands by using the run argument

```shell
pipenv run python --version
```

As pipenv also offers package manager capabilities we no londer need to
use pip for package installation/removal.\

```shell
pipenv install pylint
```

To update *Pipfile.lock* we can use the following command.

```shell
pipenv lock
```

## flask

To start a local development server of flask use the following command (always in the virtual environment)

```shell
pipenv run flask run
```

## migrations

As stated previously, migrations are managed and executed using **alembic**. Any changes to **models** script\
should also be applied to the **local** instance of postgresql database by creating and executing the migration
script locally.

**Migration on the remote db is performed automatically as long as the new migration script has been commited**

The following 3 commands are use to initialize,create and run migration scripts against the db.

```shell
pipenv run manage.py db init #Initialize db, create migration dir

pipenv run manage.py db migrate #Create a new migration script with applied changes

pipenv run manage.py db upgrade #Upgrade target db using migration script (or downgrade)
```

# Client

[TODO]

# Tracker

[TODO]
